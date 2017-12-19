package com.svlada.endpoint.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(description="营销策略设置")
public class MarkDto implements Serializable{

    @ApiModelProperty(notes="商品ID")
    private Long id;

    @ApiModelProperty(notes="商品唯一编号")
    private String code;

    @ApiModelProperty(notes="是否新品上市。true:是")
    private Boolean isNew;// 是否新品。n：否，true：是新品上市

    @ApiModelProperty(notes="是否特价优惠,true:是")
    private Boolean specialPrice;

    @ApiModelProperty(notes="特价优惠：原价")
    private Long price;// 定价

    @ApiModelProperty(notes="特价优惠：现价")
    private Long nowPrice;// 现价

    @ApiModelProperty(notes="卖家强推，true:是")
    private Boolean recommend;

    @ApiModelProperty(notes="卖家包邮，true:是")
    private Boolean mailFree;//true:是卖家包邮

    @ApiModelProperty(notes="首页循环滚动，true:是")
    private Boolean cyclic;//true:首页循环滚动


    public Boolean getCyclic() {
        return cyclic;
    }

    public void setCyclic(Boolean cyclic) {
        this.cyclic = cyclic;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getNew() {
        return isNew;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    public Boolean getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(Boolean specialPrice) {
        this.specialPrice = specialPrice;
    }

    public Boolean getRecommend() {
        return recommend;
    }

    public void setRecommend(Boolean recommend) {
        this.recommend = recommend;
    }

    public Boolean getMailFree() {
        return mailFree;
    }

    public void setMailFree(Boolean mailFree) {
        this.mailFree = mailFree;
    }
}
