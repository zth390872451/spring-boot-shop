package com.svlada.entity;

import javax.persistence.*;

/**
 * @author xmc
 * 微信支付通知
 */
@Entity
@Table(name = "wxpay_notify")
public class WxpayNotify {

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private static final long serialVersionUID = -2528515158221519608L;
	/**
	 * 微信支付订单号
	 */
	private String transactionId;
	/**
	 * 支付完成时间
	 */
	private String timeEnd;
	/**
	 * 总金额(单位：分)
	 */
	private int totalFee;
	/**
	 * 付款银行
	 */
	private String bankType;
	
	//-------------临时属性-------------------//
	//订单号
	private String outTradeNo;
	private String mchId;
	//-------------临时属性-------------------//

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public WxpayNotify(){}
	
	public WxpayNotify(String transactionId, String timeEnd, int totalFee, String bankType) {
		this.transactionId = transactionId;
		this.timeEnd = timeEnd;
		this.totalFee = totalFee;
		this.bankType = bankType;
	}
	
	@Column(length = 32,nullable = false)
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	@Column(length = 14,nullable = false)
	public String getTimeEnd() {
		return timeEnd;
	}
	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}
	public int getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(int totalFee) {
		this.totalFee = totalFee;
	}
	public String getBankType() {
		return bankType;
	}
	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	@Transient
	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	@Transient
	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	
}
