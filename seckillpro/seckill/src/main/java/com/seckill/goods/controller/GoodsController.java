package com.seckill.goods.controller;

import com.seckill.commons.CodeEnum;
import com.seckill.commons.ReturnObject;
import com.seckill.goods.model.Goods;
import com.seckill.goods.service.GoodsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Controller
public class GoodsController {
    @Resource
    private GoodsService goodsService;

    @RequestMapping("/")
    public String showGoodsList(Model model){
        ReturnObject<List<Goods>> returnObject= goodsService.getGoodsList();
        model.addAttribute("goodsList",returnObject.getResult());
        return "index";
    }
    @RequestMapping("/showGoodsInfo")
    public String showGoodsInfo(Integer goodsId,Model model){
        ReturnObject<Goods> returnObject= goodsService.getGoodsById(goodsId);
        model.addAttribute("goods",returnObject.getResult());
        return "goodsInfo";
    }
    @RequestMapping("/getSysTime")
    @ResponseBody
    public Object getSysTime(){

        return new ReturnObject<Long>(CodeEnum.OK,"获取系统时间成功",System.currentTimeMillis());
    }
    @RequestMapping("/getRandomName")
    @ResponseBody
    public Object getRandomName(Integer goodsId){
       Goods goods= goodsService.getGoodsById(goodsId).getResult();
       //进入if表示根据商品Id没有获取到商品数据,原因可能是用户手动拼接的请求
       if(goods==null){
           return  new ReturnObject(CodeEnum.ERROR,"商品信息异常",null);
       }
       long sysTime=System.currentTimeMillis();//获取系统时间
        //进入if表示活动没有开始,原因可能是用户手动拼接的请求
        if(sysTime<goods.getStartTime().getTime()){
            return  new ReturnObject(CodeEnum.ERROR,"活动还没有开始",null);
        }
        //进入if表示活动已经结束,原因可能是用户手动拼接的请求或用户在抢购页面停留时间过久
        if(sysTime>goods.getEndTime().getTime()){
            return  new ReturnObject(CodeEnum.ERROR,"活动已经结束",null);
        }
        return new ReturnObject<String>(CodeEnum.OK,"获取随机名成功",goods.getRandomName());
    }
    @RequestMapping("/secKill")
    @ResponseBody
    public Object secKill(Integer goodsId, String randomName, BigDecimal price){
        //当前登录用户Session中的用户Id
        Integer uid=1;
        return goodsService.secKill(goodsId,randomName,price,uid);
    }
}
