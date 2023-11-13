package com.example.Aiking.Entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="categoryId")
    private int categoryId;
    @Column(name="namecategory",length = 50, nullable = false,unique = true)
    private String nameCategory;
    @Column(name="description",length=100)
    private String description;
    @Column(name="createDate",nullable = false)
    private Date createDate;
    @Column(name="updateDate",nullable = false)
    private Date updateDate;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
