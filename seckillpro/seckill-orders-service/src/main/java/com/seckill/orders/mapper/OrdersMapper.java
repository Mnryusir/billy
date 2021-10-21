package com.seckill.orders.mapper;

import com.seckill.orders.model.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersMapper {

    int insert(Orders record);


}