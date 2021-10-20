package com.yuzhi.crm.settings.domain;

import java.io.Serializable;

public class DictionaryValue implements Serializable {
    private String id;
    private String text;
    private String value;
    private String orderNo;
    private String typeCode;

    @Override
    public String toString() {
        return "DictionaryValue{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", value='" + value + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", typeCode='" + typeCode + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }
}