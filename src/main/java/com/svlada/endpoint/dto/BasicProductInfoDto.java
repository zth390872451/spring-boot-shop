package com.svlada.endpoint.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel(description = "基本信息Dto")
public class BasicProductInfoDto implements Serializable{

    @ApiModelProperty(notes="商品名称")
    private String name;// 商品名称

    @ApiModelProperty(notes="商品的唯一编号")
    @NotNull(message = "不能为空")
    private String code;// 商品编号

    @ApiModelProperty(notes="商品定价")
    private Long price;

    @ApiModelProperty(notes="商品现价")
    private Long nowPrice;

    @ApiModelProperty(notes="商品库存")
    private Long  stock;

    @ApiModelProperty(notes="商品所属类别ID")
    private Long categoryId;//商品类别ID


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
