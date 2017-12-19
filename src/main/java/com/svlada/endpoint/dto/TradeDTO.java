package com.svlada.endpoint.dto;


import java.util.Map;

public class TradeDTO {
    private String outTradeNo;
    
    private Map<String,Object> orderInfo;
    
    
    public TradeDTO(){}

    public TradeDTO(String outTradeNo, Map<String,Object> orderInfo) {
        this.outTradeNo = outTradeNo;
        this.orderInfo = orderInfo;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Map<String, Object> getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(Map<String, Object> orderInfo) {
        this.orderInfo = orderInfo;
    }
}
