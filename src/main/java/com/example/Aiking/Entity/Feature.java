package com.example.Aiking.Entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="feature")
public class Feature {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="featureId")
    private int featureId;
    @Column(name="nameFeature",length = 50,nullable = false)
    private String nameFeature;
    @Column(name="description",length = 50,nullable = false)
    private String description;
    @Column(name="createDate",nullable = false)
    private Date createDate;
    @Column(name="updateDate",nullable = false)
    private Date updateDate;

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

    public int getFeatureId() {
        return featureId;
    }

    public void setFeatureId(int featureId) {
        this.featureId = featureId;
    }

    public String getNameFeature() {
        return nameFeature;
    }

    public void setNameFeature(String nameFeature) {
        this.nameFeature = nameFeature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
