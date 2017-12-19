package com.svlada.endpoint.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(description = "收货地址")
public class AddressDto implements Serializable{

    @ApiModelProperty(notes="地址记录ID",required = false)
    private Long id;

    @ApiModelProperty(notes="活动ID",required = false)
    private String name;

    @ApiModelProperty(notes="省(直辖市)",required = false)
    private String province;

    @ApiModelProperty(notes="县市",required = false)
    private String city;

    @ApiModelProperty(notes="乡镇",required = false)
    private String area;

    @ApiModelProperty(notes="地址",required = false)
    private String pcaDetail;

    @ApiModelProperty(notes="详细地址",required = false)
    private String address;

    @ApiModelProperty(notes="邮编",required = false)
    private String zip;

    @ApiModelProperty(notes="收货电话",required = false)
    private String phone;

    @ApiModelProperty(notes="收货人手机号码",required = false)
    private String mobile;

    @ApiModelProperty(notes="是否默认收货地址,true：是",required = false)
    private Boolean isDefault;


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

    public String getPcaDetail() {
        return pcaDetail;
    }

    public void setPcaDetail(String pcaDetail) {
        this.pcaDetail = pcaDetail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }
}
