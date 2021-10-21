package com.seckill.goods.service;

import com.seckill.goods.model.Goods;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

public interface GoodsService {
    List<Goods> getGoodsList();

    Goods getGoodsById(Integer goodsId);

    int secKill(Integer goodsId, String randomName, BigDecimal price, Integer uid);
}
