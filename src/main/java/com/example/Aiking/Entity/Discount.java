package com.example.Aiking.Entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="discount")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="discountId")
    private int discountId;
    @Column(name="discountPercent" , nullable = false)
    private Double discountPercent;
    @Column(name="active" , nullable = false)
    private Boolean active;
    @Column(name="createDate",nullable = false)
    private Date createDate;
    @Column(name="updateDate",nullable = false)
    private Date updateDate;

    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    public Double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
