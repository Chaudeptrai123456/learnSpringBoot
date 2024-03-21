package com.example.serverUser.DTO;

public class Product_InventoryDTO {

    private Long productID;
    private Long inventoryID;
    private Double cost;
    private int quanlity;

    public Product_InventoryDTO(Long productID, Long inventoryID, Double cost, int quanlity) {
        this.productID = productID;
        this.inventoryID = inventoryID;
        this.cost = cost;
        this.quanlity = quanlity;
    }

    public Long getProductID() {
        return productID;
    }

    public void setProductID(Long productID) {
        this.productID = productID;
    }

    public Long getInventoryID() {
        return inventoryID;
    }

    public void setInventoryID(Long inventoryID) {
        this.inventoryID = inventoryID;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public int getQuanlity() {
        return quanlity;
    }

    public void setQuanlity(int quanlity) {
        this.quanlity = quanlity;
    }
}
