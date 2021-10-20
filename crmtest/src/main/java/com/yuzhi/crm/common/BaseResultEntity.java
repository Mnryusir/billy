package com.yuzhi.crm.common;
import java.io.Serializable;


/**
 * 抽取公共返回值结果集
 * @param <T>
 */
public class BaseResultEntity<T> implements Serializable {
    private Integer code;
    private String msg;
    private T data;
//    private Object data;


    public BaseResultEntity() {
    }

    public BaseResultEntity(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseResultEntity(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 返回成功的标记和信息
     *      {
     *          code:10000,
     *          msg:操作成功
     *      }
     * @return
     */
    public static BaseResultEntity ok(){
        return new  BaseResultEntity(10000,"操作成功");
    }

    /**
     * 返回成功的标记和信息
     *      {
     *          code:10000,
     *          msg:操作成功,
     *          data:[...]
     *      }
     * @return
     */
    public static BaseResultEntity ok(Integer code, String msg, Object data){
        return new  BaseResultEntity(code,msg,data);
    }

    /**
     * 返回失败的标记和信息
     *      {
     *          code:10001,
     *          msg:操作成功
     *      }
     * @return
     */
    public static BaseResultEntity error(String msg){
        return new  BaseResultEntity(10001,msg);
    }

    /**
     * 返回失败的标记和信息
     *      {
     *          code:xxx,
     *          msg:操作成功
     *      }
     * @return
     */
    public static BaseResultEntity error(Integer code,String msg){
        return new  BaseResultEntity(code,msg);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

