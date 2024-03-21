package com.example.serverUser.DTO;

import com.example.serverUser.Entity.Feature;
import com.example.serverUser.Entity.Inventory;
import com.example.serverUser.Entity.Product_Inventory;

import java.util.Set;

public class ProductDTO {

    private String nameProduct;
    private Double cost;

    private int quantity;
    private Long inventoryID;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getInventoryID() {
        return inventoryID;
    }

    public void setInventoryID(Long inventoryID) {
        this.inventoryID = inventoryID;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }
}
