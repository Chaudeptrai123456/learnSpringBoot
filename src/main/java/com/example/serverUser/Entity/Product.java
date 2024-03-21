package com.example.serverUser.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;
@Entity
@Table(name="product")
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productID;
    @Column(name="cost",nullable = false)
    private Double cost;
    @Column(name="name_product",nullable = false,length = 50)
    private String nameProduct;
    @ManyToMany()
    @JoinTable(
            name = "product_feature",
            joinColumns = @JoinColumn(name="product_id"),
            inverseJoinColumns = @JoinColumn(name="feature_id")
    )
    @JsonIgnore
    private Set<Feature> listFeature;

    @OneToMany(mappedBy = "product" , cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Product_Inventory> listInventory;

    public void addProduct_InventoryToList(Product_Inventory productInventory) {
        this.listInventory.add(productInventory);
    }


    public Long getProductID() {
        return productID;
    }

    public void setProductID(Long productID) {
        this.productID = productID;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public Set<Feature> getListFeature() {
        return listFeature;
    }

    public void setListFeature(Set<Feature> listFeature) {
        this.listFeature = listFeature;
    }

    public Set<Product_Inventory> getListInventory() {
        return listInventory;
    }

    public void setListInventory(Set<Product_Inventory> listInventory) {
        this.listInventory = listInventory;
    }
}
