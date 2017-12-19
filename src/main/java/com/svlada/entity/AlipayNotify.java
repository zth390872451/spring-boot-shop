package com.svlada.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author xmc
 * ali支付通知
 */
@Entity
@Table(name = "alipay_notify")
public class AlipayNotify implements Serializable{

	private static final long serialVersionUID = 8946372528066213629L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	/**
	 * 通知校验ID
	 */
	private String notifyId;
	/**
	 * 通知时间
	 */
	private Date notifyTime;
	/**
	 * 通知类型
	 */
	private String notifyType;
	/**
	 * 支付宝交易号
	 */
	private String tradeNo;
	/**
	 * 支付类型(默认值：1 购买商品)
	 */
	private int paymentType = 1;
	/**
	 * 交易金额
	 */
	private double totalFee;
	/**
	 * 买家支付宝用户号
	 */
	private String buyerId;
	/**
	 * 买家支付宝账号
	 */
	private String buyerEmail;
	/**
	 * 交易创建时间
	 */
	private Date gmtCreate;
	/**
	 * 交易付款时间
	 */
	private Date gmtPayment;
	/**
	 * 交易状态
	 */
	private String tradeStatus;
	
	//-------------临时属性-------------------//
	//订单号
	private String outTradeNo;
	//卖家支付宝用户号
	private String sellerId;
	//-------------临时属性-------------------//
	
	public AlipayNotify() {}
	
	public AlipayNotify(String notifyId, Date notifyTime, String notifyType, String tradeNo, double totalFee, String buyerId, String buyerEmail, Date gmtCreate, Date gmtPayment) {
		this.notifyId = notifyId;
		this.notifyTime = notifyTime;
		this.notifyType = notifyType;
		this.tradeNo = tradeNo;
		this.totalFee = totalFee;
		this.buyerId = buyerId;
		this.buyerEmail = buyerEmail;
		this.gmtCreate = gmtCreate;
		this.gmtPayment = gmtPayment;
	}
	
	@Column(length = 64,nullable = false,unique = true,updatable = false)
	public String getNotifyId() {
		return notifyId;
	}
	public void setNotifyId(String notifyId) {
		this.notifyId = notifyId;
	}
	public Date getNotifyTime() {
		return notifyTime;
	}
	public void setNotifyTime(Date notifyTime) {
		this.notifyTime = notifyTime;
	}
	public String getNotifyType() {
		return notifyType;
	}
	public void setNotifyType(String notifyType) {
		this.notifyType = notifyType;
	}
	
	@Column(length = 64,nullable = false,unique = true)
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public int getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(int paymentType) {
		this.paymentType = paymentType;
	}
	public double getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(double totalFee) {
		this.totalFee = totalFee;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(length = 30)
	public String getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	
	@Column(length = 100)
	public String getBuyerEmail() {
		return buyerEmail;
	}
	public void setBuyerEmail(String buyerEmail) {
		this.buyerEmail = buyerEmail;
	}
	public Date getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	public Date getGmtPayment() {
		return gmtPayment;
	}
	public void setGmtPayment(Date gmtPayment) {
		this.gmtPayment = gmtPayment;
	}
	
	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	@Transient
	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	@Transient
	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	
}
