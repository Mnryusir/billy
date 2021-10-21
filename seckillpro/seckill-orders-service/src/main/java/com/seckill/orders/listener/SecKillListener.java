package com.seckill.orders.listener;

import com.alibaba.fastjson.JSONObject;
import com.seckill.commons.Constants;
import com.seckill.orders.model.Orders;
import com.seckill.orders.service.OrderService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SecKillListener {
    @Resource
    private OrderService orderService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @RabbitListener(bindings = {@QueueBinding(value = @Queue("secKillQueue"),exchange = @Exchange(name = "secKillExchange",type = "fanout"))})
    public void secKillMessageListener(String ordersMessage){

        Orders orders= JSONObject.parseObject(ordersMessage,Orders.class);
        //添加订单 ,返回订单下单状态,0为下单成功
       int result= orderService.addSecKill(orders);
       if(result==0){
           //订单添加成功,将Redis中的订单备份数据删除
           stringRedisTemplate.opsForZSet().remove(Constants.ORDERS,ordersMessage);
       }
    }
}
