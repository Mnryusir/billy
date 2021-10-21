package com.seckill.timer;

import com.seckill.commons.Constants;
import com.seckill.goods.model.Goods;
import com.seckill.service.GoodsService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Component
public class SecKillTimer {
    @Resource
    private GoodsService goodsService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private AmqpTemplate amqpTemplate;
    /**
     * 配置定时任务,每5秒钟执行一次将需要秒杀的商品初始化到Redis中
     * 注意:
     *   实际工作时不能使每5秒钟一次,应该是在活动即将开始前或固定时间例如每天23:55分执行一次
     *   将未来一天的所有数据全部初始化到Redis中
     *   我们使用5秒钟一次仅仅为了方便测试
     */
    @Scheduled(cron = "0/5 * * * * *")
    public void initSecKillDataToRedis(){
        //获取所有商品信息
        //注意:实际工作时不能是获取所有的数据,而是要根据系统的时间获取即将进行活动的商品信息
       List<Goods> goodsList= goodsService.getGoodsList().getResult();
       goodsList.forEach(goods -> {
           /**
            * 使用统一Key前缀+商品随机名作为Key ,使用商品库存作为value初始化数据到Redis中
            * setIfAbsent 方法在向Redis中写入数据时,
            * 如果Key存在则放弃写入返回false
            * 如果Key不存在则完成写入返回true
            */
           String key=Constants.GOODS_STORE+goods.getRandomName();
           String value=goods.getStore()+"";
           stringRedisTemplate.opsForValue().setIfAbsent(key,value);
       });
    }

    /**
     * 配置一个定时任务,每5秒钟执行一次用于扫描Redis中的订单备份数据
     * 注意:
     *   实际工作时不能是每5秒钟一次,应该是更长的时间例如5分钟或15分钟等等
     */
    @Scheduled(cron = "0/5 * * * * *")
    public void diaoDan(){
        //使用当前系统时间前15分钟毫秒值作为获取掉单数据的最大分数
       long maxScore=System.currentTimeMillis()-1000*60*15;
       Set<String> ordersSet= stringRedisTemplate.opsForZSet().rangeByScore(Constants.ORDERS,0,maxScore);
       ordersSet.forEach(orders->{
           amqpTemplate.convertAndSend("secKillExchange","",orders);
       });
    }
}
