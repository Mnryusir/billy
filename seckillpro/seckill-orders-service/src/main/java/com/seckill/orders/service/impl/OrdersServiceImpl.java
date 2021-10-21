package com.seckill.orders.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.seckill.commons.Constants;
import com.seckill.orders.mapper.OrdersMapper;
import com.seckill.orders.model.Orders;
import com.seckill.orders.service.OrderService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Date;

@Service
public class OrdersServiceImpl implements OrderService {
    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public int addSecKill(Orders orders) {
        orders.setStatus(1);
        orders.setBuyNum(1);
        orders.setCreateTime(new Date());
        orders.setOrderMoney(orders.getBuyPrice().multiply(new BigDecimal(orders.getBuyNum())));
        try {
            //将订单数据存入数据库,这里会出现一个异常DuplicateKeyException,这个异常是Spring提供的表示违反了数据库的唯一约束
            ordersMapper.insert(orders);
            Duration timeout=Duration.ofSeconds(60*5);
            //将订单结果存入Redis用于通知前段浏览器进行支付的,这个数据用户不是100%能够获取到因此必须添加超时时间
            stringRedisTemplate.opsForValue().set(Constants.ORDERS_RESULT+orders.getGoodsId()+":"+orders.getUid(), JSONObject.toJSONString(orders),timeout);
        } catch (DuplicateKeyException e) {
            //进入这里表示违反数据库的唯一约束
            System.out.println(e.getClass());
            return 0;
        }
        return 0;
    }

    @Override
    public Orders getOrdersResult(Integer goodsId, Integer uid) {
        String orderStr= stringRedisTemplate.opsForValue().get(Constants.ORDERS_RESULT+goodsId+":"+uid);
        return orderStr==null?null:JSONObject.parseObject(orderStr,Orders.class);
    }
}
