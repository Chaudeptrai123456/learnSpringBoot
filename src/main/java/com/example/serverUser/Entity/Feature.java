package com.example.serverUser.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name="feature")
@Getter
@Setter
public class Feature {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long featureId;
    @Column(name="namefeature",nullable = false,length = 50)
    private String nameFeature;
    @Column(name="feature",nullable = false,length = 50)
    private String feature;
    @ManyToMany(mappedBy="listFeature")
    private Set<Product> productList;

    public Long getFeatureId() {
        return featureId;
    }

    public void setFeatureId(Long featureId) {
        this.featureId = featureId;
    }

    public String getNameFeature() {
        return nameFeature;
    }

    public void setNameFeature(String nameFeature) {
        this.nameFeature = nameFeature;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public Set<Product> getProductList() {
        return productList;
    }

    public void setProductList(Set<Product> productList) {
        this.productList = productList;
    }
}
