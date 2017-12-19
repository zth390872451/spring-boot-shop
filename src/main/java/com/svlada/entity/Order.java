package com.svlada.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "User_Order")
public class Order implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE }, mappedBy = "order")
    //这里配置关系，并且确定关系维护端和被维护端。mappBy表示关系被维护端，只有关系端有权去更新外键。
    // 这里还有注意OneToMany默认的加载方式是赖加载。当看到设置关系中最后一个单词是Many，那么该加载默认为懒加载
    private Set<OrderItem> items = new HashSet<OrderItem>();

    @ManyToOne
    @JoinColumn(name="user_id")//这里设置JoinColum设置了外键的名字，并且Order是关系维护端
    private User user;

    @OneToOne
    @JoinColumn(name = "order_ship_id")//外键关联Order表
    private OrderShip orderShip;
    /**
     * 订单号唯一[商户订单号]
     */
    private String outTradeNo;
    private Integer payStatus;//订单的支付状态 0：尚未支付 1：支付成功 2:订单支付超时(失效) 3:支付失败
    public static final Integer pay_status_init = 0;
    public static final Integer pay_status_success = 1;
    public static final Integer pay_status_overtime = 2;
    public static final Integer pay_status_failed = 3;
    /**
     * 商品名称
     */
    private String body;
    /**
     * 商品描述(该笔订单的备注、描述、明细等)
     */
    private String details;
    /**
     * 对应ali支付通知
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alipay_notify_id")
    private AlipayNotify alipayNotify;

    /**
     * 对应wxpay支付通知
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wxpay_notify_id")
    private WxpayNotify wxpayNotify;
    /**
     * 交易支付时间
     */
    private Date paymentDate;
    /**
     * 支付金额
     */
    private Long totalMoney;//订单的总金额 = 订单项的总金额(商品费用+快递费用)

    private String shareId;//分享人OpenId

    private Boolean shareFlag =false;//佣金是否结算过 false:未计算过

    public enum PayType{
        ALIPAY,WEINXIPAY
    }

    public Boolean getShareFlag() {
        return shareFlag;
    }

    public void setShareFlag(Boolean shareFlag) {
        this.shareFlag = shareFlag;
    }

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    public OrderShip getOrderShip() {
        return orderShip;
    }

    public void setOrderShip(OrderShip orderShip) {
        this.orderShip = orderShip;
    }

    private Date createDate = new Date();

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public AlipayNotify getAlipayNotify() {
        return alipayNotify;
    }

    public void setAlipayNotify(AlipayNotify alipayNotify) {
        this.alipayNotify = alipayNotify;
    }

    public WxpayNotify getWxpayNotify() {
        return wxpayNotify;
    }

    public void setWxpayNotify(WxpayNotify wxpayNotify) {
        this.wxpayNotify = wxpayNotify;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Long getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Long totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Set<OrderItem> getItems() {
        return items;
    }

    public void setItems(Set<OrderItem> items) {
        this.items = items;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 该方法用于向order中加order项
     */
    public void addOrderItem(OrderItem orderItem) {
        orderItem.setOrder(this);//用关系维护端来维护关系
        this.items.add(orderItem);
    }

}
