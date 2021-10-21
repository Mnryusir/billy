package com.seckill.goods.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.seckill.commons.Constants;
import com.seckill.goods.mapper.GoodsMapper;
import com.seckill.goods.model.Goods;
import com.seckill.goods.service.GoodsService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private AmqpTemplate amqpTemplate;
    /**
     * 获取商品列表,
     * 获取所有商品信息,实际工作时应该根据当前系统的时间来获取活动正在进行中或即将开始活动的商品列表
     * 这个方法可能并发量比较高因此必要时需要从Redis中获取数据
     *
     * @return
     */
    public List<Goods> getGoodsList() {
        return goodsMapper.selectAll();
    }

    /**
     * 根据商品Id获取商品信息
     * 这个方法并发来那个可能会很高,因此必要时需要从Redis中获取数据
     *
     * @param goodsId 商品Id
     * @return 返回商品对象
     */
    public Goods getGoodsById(Integer goodsId) {
        return goodsMapper.selectByPrimaryKey(goodsId);
    }

    /**
     * 秒杀的主业务方法,主要实现减少库存 下单 限购 防止超卖等等.由于并发量比较因此不能直接操作数据库
     * 需要利用Redis和MQ完成任务
     *
     * @param goodsId    商品Id
     * @param randomName 商品随机名
     * @param price      商品价格
     * @param uid        用户Id
     * @return 下单的状态码
     * 0 表示下单成功
     * 1 表示商品信息异常
     * 2 表示没有足够的库存
     * 3 表示用户以及有了购买记录
     * 4 表示服务器限流了
     */
    public int secKill(Integer goodsId, String randomName, BigDecimal price, Integer uid) {
        /**
         * 判断是否有库存
         */
        String storeStr = stringRedisTemplate.opsForValue().get(Constants.GOODS_STORE + randomName);
        //进入if表示根据商品随机名称没有获取到商品信息,原因可能是用户手动拼接的请求
        if (storeStr == null || "".equals(storeStr)) {
            return 1;
        }
        //进入if表示没有库存
        if (Integer.valueOf(storeStr) <= 0) {
            return 2;
        }
        //程序到了这里,暂时有库存但是不一定能买到商品
        /**
         * 用户限购控制
         */
        //使用统一Key前缀+商品Id+用户Id获取用户的购买记录
        String purchaseLimits = stringRedisTemplate.opsForValue().get(Constants.PURCHASE_LIMITS + goodsId + ":" + uid);
        //进入if表示当前用户有购买记录,要立即返回
        //这里不能100%拦截所有用户请求,但是可以拦截一大部分
        if (purchaseLimits != null && !"".equals(purchaseLimits)) {
            return 3;
        }
        //程序到了这里,暂时用户没有购买记录,但是不一定能买到商品

        /**
         * 服务器限流
         */
        //利用Redis的单线程写操作,对限流计数器+1,并返回+1后的结果
        long currentLimiting = stringRedisTemplate.opsForValue().increment(Constants.CURRENT_LIMITING);
        //进入if表示当前线程对限流计数器+1后大于了服务器的阀值,需要立即返回响应拒绝用户继续访问请求
        //这个限流上限的阀值可以是固定的例如1000或商品库存的某个倍数
        if (currentLimiting > 1) {
            //限流计数器-1 ,用于回滚刚刚当前线程的那次+1操作
            stringRedisTemplate.opsForValue().decrement(Constants.CURRENT_LIMITING);
            return 4;
        }
        //暂时有库存,暂时没有购买记录,一定有限流位置,开始尝试减少库存,但是不一定能买到商品

        /**
         * 尝试减少库存
         */
        //设置true表示允许Redis使用事务
        stringRedisTemplate.setEnableTransactionSupport(true);
        //定义一个List集合,用于存在所有需要监控的Key
        //这里我们需要监控2个Key分别是商品库存用于防止超卖,以及用户购买记录防止重复购买的
        List<String> keys=new ArrayList<String>();
        keys.add(Constants.GOODS_STORE+randomName);
        keys.add(Constants.PURCHASE_LIMITS+goodsId+":"+uid);
        //定义订单Map集合,用于将数据存入MQ通知订单服务完成数据库下单,以及存入Redis实现防掉单处理
        Map ordersMap=new HashMap();
        ordersMap.put("goodsId",goodsId);
        ordersMap.put("uid",uid);
        ordersMap.put("buyPrice",price);
        List list=null;
        do {
            //设置Key监控,设置Key监控以后相当是锁定了Key所对应的数据,如果当前线程在提交事务时,如果所监控的数据被其他线程
            //先修改了,那么当前线程就会放弃事务提交
            stringRedisTemplate.watch(keys);
            //再次获取库存数据
            storeStr=stringRedisTemplate.opsForValue().get(Constants.GOODS_STORE+randomName);
            //进入if表示没有库存了
            if(Integer.valueOf(storeStr)<=0){
                //释放key监控
                stringRedisTemplate.unwatch();
                return 2;
            }
            //程序到这一定有库存,但是不一定能买到商品
            //再次获取用户购买记录
            purchaseLimits= stringRedisTemplate.opsForValue().get(Constants.PURCHASE_LIMITS+goodsId+":"+uid);
            //进入if表示用户有购买记录
            if(purchaseLimits!=null&&!"".equals(purchaseLimits)){
                //释放key监控
                stringRedisTemplate.unwatch();
                return 3;
            }
            //程序到了这里一定有库存,一定没有购买记录开始减少库存但是不一定能买到商品

            //开始事务
            stringRedisTemplate.multi();
            //减少库存
            stringRedisTemplate.opsForValue().decrement(Constants.GOODS_STORE+randomName);
            //添加购买记录
            stringRedisTemplate.opsForValue().set(Constants.PURCHASE_LIMITS+goodsId+":"+uid,"1");
            //使用统一的Key,使用订单Map集合作为value,使用当前系统时间毫秒值作为分数
            //将数据记录到Redis的ZSet集合中,用于防掉单处理,这个数据需要利用定时任务来定期扫描获取数据,如果判断存在掉单
            //则补发消息到MQ中,这个数据只有在订单完成数据库下单以后才会从Redis中删除掉
            long score=System.currentTimeMillis();
            stringRedisTemplate.opsForZSet().add(Constants.ORDERS,JSONObject.toJSONString(ordersMap),score);
            //执行事务,返回List集合,如果List集合长度大于0表示事务提交成功,如果List集合长度等于0则表示事务提交失败原因是所监控的Key
            //被其他线程所修改了,放弃了事务提交
            list=stringRedisTemplate.exec();
           //如果list集合中没有任何元素则表示事务提交失败需要循环再次尝试减少库存
        } while (list.isEmpty());
        //程序到了这里表示减少了库存 添加了限购记录

        amqpTemplate.convertAndSend("secKillExchange","", JSONObject.toJSONString(ordersMap));
        //用户离开限流人数-1
        stringRedisTemplate.opsForValue().decrement(Constants.CURRENT_LIMITING);
        return 0;
    }
}
