package com.seckill.goods.controller;

import com.seckill.commons.CodeEnum;
import com.seckill.commons.ReturnObject;
import com.seckill.goods.model.Goods;
import com.seckill.goods.service.GoodsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@RestController
public class GoodsController {
    @Resource
    private GoodsService goodsService;
    @RequestMapping("/getGoodsList")
    public Object getGoodsList(){
        List<Goods> goodsList= goodsService.getGoodsList();
        return new ReturnObject<List<Goods>>(CodeEnum.OK,"获取数据成功",goodsList);
    }
    @RequestMapping("/getGoodsById")
    public Object getGoodsById(Integer goodsId){
        Goods goods= goodsService.getGoodsById(goodsId);
        return new ReturnObject<Goods>(CodeEnum.OK,"获取数据成功",goods);
    }
    @RequestMapping("/secKill")
    public Object secKill( Integer goodsId,String randomName,BigDecimal price,Integer uid){
        /**
         * 调用秒杀业务方法,返回秒杀的下单结果
         * 0 表示下单成功
         */
        int result= goodsService.secKill(goodsId,randomName,price,uid);
        switch (result){
            case 0:
                return  new ReturnObject(CodeEnum.OK,"下单成功",null);
            case 1:
                return  new ReturnObject(CodeEnum.ERROR,"商品信息异常",null);
            case 2:
                return  new ReturnObject(CodeEnum.ERROR,"商品已被抢光",null);
            case 3:
                return  new ReturnObject(CodeEnum.ERROR,"您已购成功,请在<我的订单>中查看",null);
            case 4:
                return  new ReturnObject(CodeEnum.ERROR,"服务器繁忙请稍后再试",null);
            default:
                return  new ReturnObject(CodeEnum.ERROR,"下单失败!",null);
        }
    }

}
