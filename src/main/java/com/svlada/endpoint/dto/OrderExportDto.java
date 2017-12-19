package com.svlada.endpoint.dto;

import java.io.Serializable;
import java.util.Date;

/**
 startDate	String	否		订单的创建时间，格式：2017-10-22 19:13:00
 endDate	String	否		订单的结束时间，格式：2017-10-22 19:13:00
 payStatus	Integer	否		订单的支付状态 0：尚未支付 1：支付成功 2:支付失败
 */
public class OrderExportDto implements Serializable{

    private Date startDate;

    private Date endDate;

    private Integer payStatus;

    private String  wechatCode;

    private String orderCodes;

    public OrderExportDto(String wechatCode, String orderCodes, Date startDate, Date endDate, Integer payStatus) {
        this.wechatCode = wechatCode;
        this.orderCodes = orderCodes;
        this.startDate = startDate;
        this.endDate = endDate;
        this.payStatus = payStatus;
    }

    public String getWechatCode() {
        return wechatCode;
    }

    public void setWechatCode(String wechatCode) {
        this.wechatCode = wechatCode;
    }

    public String getOrderCodes() {
        return orderCodes;
    }

    public void setOrderCodes(String orderCodes) {
        this.orderCodes = orderCodes;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }
}
