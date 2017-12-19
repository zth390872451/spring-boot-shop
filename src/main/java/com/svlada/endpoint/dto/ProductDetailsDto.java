package com.svlada.endpoint.dto;


import com.svlada.entity.product.Category;
import com.svlada.entity.product.DetailsImage;
import com.svlada.entity.product.MajorImage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@ApiModel(description = "产品详细信息的Dto")
public class ProductDetailsDto implements Serializable{

    private Long id;
    //商品价格信息
    private Long price= 0L;// 定价
    private Long nowPrice= 0L;// 现价
    private Long stock = 0L;//库存
    private Integer status = 0;//商品状态。0：新增，1：已上架，2：已下架

    private String code;//商品编号
    private String name;// 商品名称
    private String title;//商品页面标题
    private String introduce;// 商品简介
    private String description;//页面描述
    private String searchKey;//搜索关键字

    //商品图片信息
    private List<MajorImage> majorImages;
    private List<DetailsImage> detailsImages;

    private Boolean isNew = false;// 是否新品。n：否，true：是新品上市
    private Boolean recommend = false;//true：是推荐商品
    private Long saleCount = 0L;

    public Long getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(Long saleCount) {
        this.saleCount = saleCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public List<MajorImage> getMajorImages() {
        return majorImages;
    }

    public void setMajorImages(List<MajorImage> majorImages) {
        this.majorImages = majorImages;
    }

    public List<DetailsImage> getDetailsImages() {
        return detailsImages;
    }

    public void setDetailsImages(List<DetailsImage> detailsImages) {
        this.detailsImages = detailsImages;
    }

    public Boolean getNew() {
        return isNew;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    public Boolean getRecommend() {
        return recommend;
    }

    public void setRecommend(Boolean recommend) {
        this.recommend = recommend;
    }
}
