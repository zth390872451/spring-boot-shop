package com.svlada.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 快递配送表
 */
@Entity
@Table(name="Express")
public class Express implements Serializable{

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;//快递名称

    private String code;//快递编号。EXPRESS（快递）、POST（平邮）、EMS（EMS）

    private String fee;//费用

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }
}
