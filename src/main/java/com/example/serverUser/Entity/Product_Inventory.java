package com.example.serverUser.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name="product_inventory")
@Data
@Getter
@Setter
public class Product_Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pi_id;
    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name="product_id")
    private Product product;
    @Column(name="quanlity",nullable = true)
    private int quanlity;
    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name="inventory_id")
    private Inventory inventory;
    @Column(name="datecreate",nullable = false)
    private Date dateCreate;
    @Column(name="dateupdate",nullable = false)
    private Date dateUpdate;

    public int getQuanlity() {
        return quanlity;
    }

    public void setQuanlity(int quanlity) {
        this.quanlity = quanlity;
    }

    public Product getProduct() {
        return product;
    }

    public Long getPi_id() {
        return pi_id;
    }

    public void setPi_id(Long pi_id) {
        this.pi_id = pi_id;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }
}
