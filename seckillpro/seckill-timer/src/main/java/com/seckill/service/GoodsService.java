package com.seckill.service;

import com.seckill.commons.ReturnObject;
import com.seckill.goods.model.Goods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "seckill-goods-service")
public interface GoodsService {
    @RequestMapping("/getGoodsList")
    ReturnObject<List<Goods>> getGoodsList();
}
