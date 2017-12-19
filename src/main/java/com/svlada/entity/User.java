package com.svlada.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="APP_USER")
public class User {
    @Id @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="username")
    private String username;

    @Column(name="password")
    private String password;

   /* @OneToMany(cascade={CascadeType.ALL})
    @JoinTable(name="USER_PARTNER",
            joinColumns={@JoinColumn(name="source_id",referencedColumnName="id")},
            inverseJoinColumns={@JoinColumn(name="partner_id",referencedColumnName="id")}
    )*/
//    @JsonIgnore
//    private List<User> partners;

    /*@OneToMany
    private List<Partner> partners;*/


    @OneToMany
    @JoinColumn(name="APP_USER_ID", referencedColumnName="ID")
    private List<UserRole> roles;

    private String openId;

    private String nickName;
//    private String accountType;//会员类型，qq,sinawb,alipay

    private String realName;
    private String sex;
    private Date birthday;
    private String province;
    private String city;
    private String address;
    private String headImgUrl;
    private String mobile;//手机号码
    private String email;//邮箱

    private String cardNumber;//证件号码
    private String cardType;//证件类型

    private Long balance;//余额
    private Long consumption;//消费额

    private Date registerDate;
    private Date lastLoginDate;

    private String jwtToken;

    private String webAccessToken;

    private String accessToken;

    @Column(name="member")
    private Boolean member =false;

    public Boolean getMember() {
        return member;
    }

    public void setMember(Boolean member) {
        this.member = member;
    }

    public String getWebAccessToken() {
        return webAccessToken;
    }

    public void setWebAccessToken(String webAccessToken) {
        this.webAccessToken = webAccessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public User() { }

    public User(Long id, String username, String password, List<UserRole> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }


    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getConsumption() {
        return consumption;
    }

    public void setConsumption(Long consumption) {
        this.consumption = consumption;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    /*public List<User> getPartners() {
        return partners;
    }

    public void setPartners(List<User> partners) {
        this.partners = partners;
    }*/

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<UserRole> getRoles() {
        return roles;
    }
}
