package com.seckill.orders.service;

import com.seckill.commons.ReturnObject;
import com.seckill.orders.model.Orders;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name ="seckill-orders-service" )
public interface OrdersService {
    @RequestMapping("/getOrderResult")
    ReturnObject<Orders> getOrderResult(@RequestParam Integer goodsId,@RequestParam Integer uid);
}
