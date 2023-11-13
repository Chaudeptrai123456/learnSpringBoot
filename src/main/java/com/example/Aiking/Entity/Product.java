package com.example.Aiking.Entity;

import jakarta.persistence.*;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="product")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="productId")
    private int productId;
    @Column(name="nameProduct",length = 50,nullable = false,unique = true)
    private String nameProduct;
    @Column(name="price",nullable = false)
    private Double price;
    @Column(name="createDate",nullable = false)
    private Date createDate;
    @Column(name="updateDate",nullable = false)
    private Date updateDate;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="product_feature",
            joinColumns = @JoinColumn(name = "productId", referencedColumnName = "productId"),
            inverseJoinColumns = @JoinColumn(name="featureId" , referencedColumnName = "featureId")
    )
    private Set<Feature> featureSet = new HashSet<>();
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="product_category",
            joinColumns = @JoinColumn(name="productId",referencedColumnName = "productId"),
            inverseJoinColumns = @JoinColumn(name = "categoryId",referencedColumnName = "categoryId")
    )
    private Set<Category> categorySet = new HashSet<>();
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name= "product_inventory",
            joinColumns = @JoinColumn(name="productId",referencedColumnName = "productId"),
            inverseJoinColumns = @JoinColumn(name="inventoryId",referencedColumnName = "inventoryId")
    )
    private Set<Inventory> inventorySet = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="product_discount",
            joinColumns = @JoinColumn(name = "productId",referencedColumnName = "productId"),
            inverseJoinColumns = @JoinColumn(name ="discountId", referencedColumnName = "discountId")
    )
    private Set<Discount> discountSet = new HashSet<>();

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    public Set<Feature> getFeatureSet() {
        return featureSet;
    }

    public void setFeatureSet(Set<Feature> featureSet) {
        this.featureSet = featureSet;
    }

    public Set<Category> getCategorySet() {
        return categorySet;
    }

    public void setCategorySet(Set<Category> categorySet) {
        this.categorySet = categorySet;
    }

    public Set<Inventory> getInventorySet() {
        return inventorySet;
    }

    public void setInventorySet(Set<Inventory> inventorySet) {
        this.inventorySet = inventorySet;
    }

    public Set<Discount> getDiscountSet() {
        return discountSet;
    }

    public void setDiscountSet(Set<Discount> discountSet) {
        this.discountSet = discountSet;
    }
}
