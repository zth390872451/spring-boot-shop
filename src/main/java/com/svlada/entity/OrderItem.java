package com.svlada.entity;

import com.svlada.entity.product.Product;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name="OrderItem")
public class OrderItem implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH }, optional = true)
    @JoinColumn(name="order_id")//这里设置JoinColum设置了外键的名字，并且orderItem是关系维护端
	private Order order;

    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH }, optional = true)
    @JoinColumn(name="product_id")//这里设置JoinColum设置了外键的名字，并且orderItem是关系维护端
	private Product product;

    private String name;
	private Long  unitPrice;//单价
	private Integer number;//购买数量
	private Long fee;// 配送费
    private Long itemTotalMoney;// 小计
    private Integer stockStatus;// n:库存不足；y:库存充足。默认n

//	private Long isComment;//评论ID

	/**
	 * 订单项是否存在商品库存不足
	 */
	public static final String orderdetail_lowstocks_n = "n";// 不存在
	public static final String orderdetail_lowstocks_y = "y";// 库存不足

	/**
	 * 订单项是否已评论
	 */
	public static final String orderdetail_isComment_n = "n";// 未评论
	public static final String orderdetail_isComment_y = "y";// 已评论

    public Integer getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(Integer stockStatus) {
        this.stockStatus = stockStatus;
    }

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

    public Long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Long unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Long getFee() {
        return fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }

    public Long getItemTotalMoney() {
        return itemTotalMoney;
    }

    public void setItemTotalMoney(Long itemTotalMoney) {
        this.itemTotalMoney = itemTotalMoney;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
