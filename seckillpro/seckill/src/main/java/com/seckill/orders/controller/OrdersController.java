package com.seckill.orders.controller;

import com.seckill.commons.ReturnObject;
import com.seckill.orders.model.Orders;
import com.seckill.orders.service.OrdersService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class OrdersController {
    @Resource
    private OrdersService ordersService;
    @RequestMapping("/getOrderResult")
    public @ResponseBody Object getOrderResult(Integer goodsId){
        Integer uid=1;
        ReturnObject<Orders> returnObject= ordersService.getOrderResult(goodsId,uid);
        return returnObject;
    }
}
