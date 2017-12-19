package com.svlada.endpoint.dto;

import java.io.Serializable;

public class CartProductDto implements Serializable{

    private String productName;//商品名称

    private Long price;//定价

    private Long nowPrice;//现价

    private Long stock;//库存

    private String introduce;// 商品简介

    private Boolean mailFree;//是否包邮

    private String majorImageUrl;//首页的第一张展示图片

    public CartProductDto() {
    }

    public CartProductDto(String productName, Long price, Long nowPrice, Long stock, String introduce, Boolean mailFree, String majorImageUrl) {
        this.productName = productName;
        this.price = price;
        this.nowPrice = nowPrice;
        this.stock = stock;
        this.introduce = introduce;
        this.mailFree = mailFree;
        this.majorImageUrl = majorImageUrl;
    }

    public String getMajorImageUrl() {
        return majorImageUrl;
    }

    public void setMajorImageUrl(String majorImageUrl) {
        this.majorImageUrl = majorImageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getNowPrice() {
        return nowPrice;
    }

    public void setNowPrice(Long nowPrice) {
        this.nowPrice = nowPrice;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public Boolean getMailFree() {
        return mailFree;
    }

    public void setMailFree(Boolean mailFree) {
        this.mailFree = mailFree;
    }

}
