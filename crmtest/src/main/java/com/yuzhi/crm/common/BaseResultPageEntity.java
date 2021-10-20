package com.yuzhi.crm.common;



import java.io.Serializable;
import java.util.List;

public class BaseResultPageEntity<T> implements Serializable {
    private Integer code;
    private String msg;
    private List<T> data;
    //定义分页组件所需要的参数
    private Integer pageNo;
    private Integer pageSize;
    private Long totalPages;
    private Long totalCount;
    public BaseResultPageEntity() {
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public BaseResultPageEntity(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseResultPageEntity(Integer code, String msg, List<T> data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public static BaseResultPageEntity ok(){

        return new BaseResultPageEntity(10000,"操作成功");
    }

    public static BaseResultPageEntity ok(Integer code,String msg,List data,Integer pageNo,
                                          Integer pageSize,
                                          Long totalPages,
                                          Long totalCount){
        return new BaseResultPageEntity(code,msg,data,pageNo,pageSize,totalPages,totalCount);
    }
    public static BaseResultPageEntity error(String msg){
        return new BaseResultPageEntity(10001,msg);
    }
    public static BaseResultPageEntity error(Integer code,String msg){
        return new BaseResultPageEntity(code,msg);
    }

    @Override
    public String toString() {
        return "BaseResultPageEntity{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", totalPages=" + totalPages +
                ", totalCount=" + totalCount +
                '}';
    }

    public BaseResultPageEntity(Integer code, String msg, List<T> data, Integer pageNo, Integer pageSize, Long totalPages, Long totalCount) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.totalCount = totalCount;
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

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
