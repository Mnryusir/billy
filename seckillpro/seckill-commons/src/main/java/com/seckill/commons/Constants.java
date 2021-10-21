package com.seckill.commons;

/**
 * 常量类,主要用于设置系统中所需要的所有的静态常量数据
 */
public class Constants {
    //商品库存在Redis中的统一key前缀
    public static final String GOODS_STORE="GOODS_STORE:";
    //用户限购记录在Redis中的统一Key前缀
    public static final String PURCHASE_LIMITS="PURCHASE_LIMITS:";
    //限流计数器在Redis中的key
    public static final String CURRENT_LIMITING="CURRENT_LIMITING";
    //订单备份数据在Redis中的key
    public static final String ORDERS="ORDERS";
    //订单结果在Redis中的统一Key前缀
    public static final String ORDERS_RESULT="ORDERS_RESULT:";

}
