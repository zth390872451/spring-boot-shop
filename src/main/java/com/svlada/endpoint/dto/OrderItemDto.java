package com.svlada.endpoint.dto;

import java.io.Serializable;

public class OrderItemDto implements Serializable{

    private Long productId;

    private String name;

    private Integer  number;

    private Long unitPrice;

    private Long itemTotalMoney;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Long unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Long getItemTotalMoney() {
        return itemTotalMoney;
    }

    public void setItemTotalMoney(Long itemTotalMoney) {
        this.itemTotalMoney = itemTotalMoney;
    }
}
