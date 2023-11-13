package com.example.Aiking.Entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "inventoryId")
    private int inventoryId;
    @Column(name = "quantity",nullable = false)
    private int quantity;
    @Column(name = "createDate",nullable = false)
    private Date createDate;
    @Column(name = "updateDate",nullable = false)
    private Date updateDate;

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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
