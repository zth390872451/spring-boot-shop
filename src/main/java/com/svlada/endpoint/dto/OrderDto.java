package com.svlada.endpoint.dto;


import java.io.Serializable;

public class OrderDto implements Serializable{

    private Long id;

    private Long addressId;

    private OrderItemDto[] orderItemDtos;

    private Long totalMoney;

    private String remark;//备注

    private Boolean isFromCart = false;//是否来自购物车 true:是，否：不是

    private String shareId;

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    public Boolean isFromCart() {
        return isFromCart;
    }

    public void setIsFromCart(Boolean isFromCart) {
        this.isFromCart = isFromCart;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Long totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public OrderItemDto[] getOrderItemDtos() {
        return orderItemDtos;
    }

    public void setOrderItemDtos(OrderItemDto[] orderItemDtos) {
        this.orderItemDtos = orderItemDtos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
