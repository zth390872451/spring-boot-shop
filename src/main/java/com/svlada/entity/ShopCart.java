package com.svlada.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 购物车表
 */
@Entity
@Table(name="ShopCart")
public class ShopCart implements Serializable{

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;//购物车的主人ID

    private Long productId ;//购物车中的商品ID

    private Long number;//购买数量

    private Date createDate;//添加到购物车的时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
