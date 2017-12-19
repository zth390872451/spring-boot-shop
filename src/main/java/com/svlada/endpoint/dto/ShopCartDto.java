package com.svlada.endpoint.dto;


import java.io.Serializable;

public class ShopCartDto implements Serializable{

   private Long productId;//商品ID

   private Long number;//商品数量

   private String name;//商品名称

    private Long nowPrice;//现价

    private String desc;//描述信息

    private String imgUrl;//图片信息

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNowPrice() {
        return nowPrice;
    }

    public void setNowPrice(Long nowPrice) {
        this.nowPrice = nowPrice;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }
}
