package com.seckill.orders.service;

import com.seckill.orders.model.Orders;

public interface OrderService {
    int addSecKill(Orders orders);

    Orders getOrdersResult(Integer goodsId, Integer uid);
}
