package com.svlada.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="Partner")
public class Partner implements Serializable{

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name="share_user_id")
    private User shareUser;//分享人【合作伙伴】ID

    //根据分享人链接登陆的用户
    private Long userId;

    private Date createDate;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public User getShareUser() {
        return shareUser;
    }

    public void setShareUser(User shareUser) {
        this.shareUser = shareUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
