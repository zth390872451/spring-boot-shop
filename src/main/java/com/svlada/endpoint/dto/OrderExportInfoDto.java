package com.svlada.endpoint.dto;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import java.io.Serializable;

@ExcelTarget("OrderInfoDto")
public class OrderExportInfoDto implements Serializable{
    /**
     * 订单ID
     */
    @Excel(name = "编号", orderNum = "1", mergeVertical = true, isImportField = "id")
    private Long id;
    /**
     * 微信支付订单号
     */
    @Excel(name = "微信支付订单号", orderNum = "2", mergeVertical = true, isImportField = "transactionId")
    private String transactionId;
    /**
     * 商品名称
     */
    @Excel(name = "商品名称", orderNum = "3", mergeVertical = true, isImportField = "body")
    private String body;
    /**
     * 商品描述(该笔订单的备注、描述、明细等)
     */
    @Excel(name = "商品名称", orderNum = "4", mergeVertical = true, isImportField = "body")
    private String details;

    /**
     * 交易支付时间
     */
    @Excel(name = "交易支付时间", orderNum = "5", mergeVertical = true, isImportField = "交易支付时间")
    private String paymentDate;
    /**
     * 支付金额
     */
    @Excel(name = "支付金额", orderNum = "6", mergeVertical = true, isImportField = "支付金额")
    private Long totalMoney;

    /**
     * 订单号唯一[商户订单号]
     */
    @Excel(name = "订单号唯一[商户订单号]", orderNum = "7", mergeVertical = true, isImportField = "商户订单号")
    private String outTradeNo;
    private Integer payStatus;//订单的支付状态 0：尚未支付 1：支付成功 2:订单支付超时(失效) 3:支付失败[暂时无用]

    @Excel(name = "收货人", orderNum = "8", mergeVertical = true, isImportField = "收货人")
    private String consigneeName;//收货人
    @Excel(name = "详细地址", orderNum = "9", mergeVertical = true, isImportField = "详细地址")
    private String consigneeAddress;//详细地址
    @Excel(name = "省份", orderNum = "10", mergeVertical = true, isImportField = "省份")
    private String province;//
    @Excel(name = "城市", orderNum = "11", mergeVertical = true, isImportField = "城市")
    private String city;//城市
    @Excel(name = "收货人", orderNum = "12", mergeVertical = true, isImportField = "收货人")
    private String area;//区域
    @Excel(name = "座机", orderNum = "13", mergeVertical = true, isImportField = "座机")
    private String phone;//座机
    @Excel(name = "手机", orderNum = "14", mergeVertical = true, isImportField = "手机")
    private String tel;//手机
    @Excel(name = "性别", orderNum = "15", mergeVertical = true, isImportField = "性别")
    private String sex;//性别
    @Excel(name = "备注", orderNum = "16", mergeVertical = true, isImportField = "备注")
    private String remark;//备注
    @Excel(name = "发件人", orderNum = "17", mergeVertical = true, isImportField = "发件人姓名")
    private String sendUser ="品纯电子商务（广州）有限公司";
    @Excel(name = "发件人地址", orderNum = "18", mergeVertical = true, isImportField = "发件人地址")
    private String sendAddress="广州番禺区南村镇里仁洞新村东二街5号";
    @Excel(name = "发件人联系方式", orderNum = "19", mergeVertical = true, isImportField = "发件人联系方式")
    private String sendTel ="18802052233";
    @Excel(name = "分享人姓名", orderNum = "20", mergeVertical = true, isImportField = "分享人姓名")
    private String shareUserName ;
    @Excel(name = "分享人openId", orderNum = "21", mergeVertical = true, isImportField = "分享人openId")
    private String shareUserOpenId ;
    private Boolean shareFlag;
    private Boolean exportFlag = false;//是否导出报表

    public Boolean getExportFlag() {
        return exportFlag;
    }

    public void setExportFlag(Boolean exportFlag) {
        this.exportFlag = exportFlag;
    }

    public Boolean getShareFlag() {
        return shareFlag;
    }

    public void setShareFlag(Boolean shareFlag) {
        this.shareFlag = shareFlag;
    }

    public String getShareUserName() {
        return shareUserName;
    }

    public void setShareUserName(String shareUserName) {
        this.shareUserName = shareUserName;
    }

    public String getShareUserOpenId() {
        return shareUserOpenId;
    }

    public void setShareUserOpenId(String shareUserOpenId) {
        this.shareUserOpenId = shareUserOpenId;
    }

    public String getSendUser() {
        return sendUser;
    }

    public void setSendUser(String sendUser) {
        this.sendUser = sendUser;
    }

    public String getSendAddress() {
        return sendAddress;
    }

    public void setSendAddress(String sendAddress) {
        this.sendAddress = sendAddress;
    }

    public String getSendTel() {
        return sendTel;
    }

    public void setSendTel(String sendTel) {
        this.sendTel = sendTel;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
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


    public Long getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Long totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }
}
