package com.svlada.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="Address")
public class Address implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonIgnore
	private User user;

	private String name;//收货人姓名
	private String province;
	private String city;
	private String area;
	private String pcaDetail;
	private String address;
	private String zip;
	private String phone;
	private String mobile;
	private Boolean isDefault = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public static long getSerialVersionUID() {
		return serialVersionUID;
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

	public Boolean getDefault() {
		return isDefault;
	}

	public void setDefault(Boolean aDefault) {
		isDefault = aDefault;
	}
}
