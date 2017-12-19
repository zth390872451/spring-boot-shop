package com.svlada.endpoint.dto.resp;

public class ProductInfo {

    //商品价格信息
    private Long price;// 定价
    private Long nowPrice;// 现价
    private Long stock;//库存
    private Integer status;//商品状态。0：新增，1：已上架，2：已下架

    //商品信息
    private String code;//商品编号
    private String name;// 商品名称
    private String title;//商品页面标题
    private String introduce;// 商品简介
    private String description;//页面描述
    private String searchKey;//搜索关键字
    //搜索条件
    private Boolean isNew = false;// 是否新品。n：否，true：是新品上市
    private Boolean specialPrice = false;// 是否特价。n：否，true：是促销商品
    private Long saleCount;//销售数量
    private Boolean recommend = false;//true：是推荐商品
    private Boolean mailFree = false;//true:是卖家包邮


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

    public Long getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(Long saleCount) {
        this.saleCount = saleCount;
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
