package com.seckill.orders.controller;

import com.seckill.commons.CodeEnum;
import com.seckill.commons.ReturnObject;
import com.seckill.orders.model.Orders;
import com.seckill.orders.service.OrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class OrdersController {
    @Resource
    private OrderService orderService;
    @RequestMapping("/getOrderResult")
    ReturnObject<Orders> getOrderResult( Integer goodsId,  Integer uid){
       Orders orders= orderService.getOrdersResult(goodsId,uid);
       if(orders==null){
           return new ReturnObject(CodeEnum.ERROR,"获取订单失败",null);
       }
        return new ReturnObject(CodeEnum.OK,"获取订单成功",orders);
    }
}
