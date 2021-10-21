package com.seckill.goods.service;

import com.seckill.commons.ReturnObject;
import com.seckill.goods.model.Goods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(name = "seckill-goods-service")
public interface GoodsService {
    @RequestMapping("/getGoodsList")
    ReturnObject<List<Goods>> getGoodsList();
    @RequestMapping("/getGoodsById")
    ReturnObject<Goods> getGoodsById(@RequestParam Integer goodsId);
    @RequestMapping("/secKill")
    ReturnObject secKill(@RequestParam Integer goodsId,
                         @RequestParam String randomName,
                         @RequestParam BigDecimal price,
                         @RequestParam Integer uid);
}
