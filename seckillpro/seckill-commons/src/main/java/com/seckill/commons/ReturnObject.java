package com.seckill.commons;

/**
 * 在为服务中返回的Json响应数据的统一封装对象
 * @param <T>
 */
public class ReturnObject<T> {
    private CodeEnum code;//响应状态码 OK表示响应成功,ERROR表示响应失败
    private String msg;//响应的信息
    private T result; //具体的响应数据

    public ReturnObject() {
    }

    public ReturnObject(CodeEnum code, String msg, T result) {
        this.code = code;
        this.msg = msg;
        this.result = result;
    }

    public CodeEnum getCode() {
        return code;
    }

    public void setCode(CodeEnum code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
