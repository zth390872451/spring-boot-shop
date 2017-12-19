package com.svlada.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.svlada.entity.product.Product;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="Activity")
public class Activity implements Serializable{

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;//活动名称

    private Date startDate;

    private Date endDate;

    private String content;//内容描述

    private Boolean status;//状态

    @OneToMany
    @JsonIgnore
    private List<Product> productList;//参与活动的产品

    private Integer maxSaleCount;//个人最大限购数量

    private Integer type;//活动类别：1、促销活动(低价、特价活动，时间限制) 2、限购活动(购买数量限制,时间限制) 3、卖家新品(卖家主推新品)

    public static final Boolean STATUS_EXPIRE = false;//下线
    public static final Boolean STATUS_NORMAL = true;//正常在线

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public Integer getMaxSaleCount() {
        return maxSaleCount;
    }

    public void setMaxSaleCount(Integer maxSaleCount) {
        this.maxSaleCount = maxSaleCount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
