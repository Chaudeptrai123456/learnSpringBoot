package se.chau.microservices.api.core.product;

import java.io.Serializable;

public class Product implements Serializable {
    private static final long serialVersionUID = 1603714798906422731L;
    private int productId;
    private String name;

    private int quantity;

    private double cost;
    private String serviceAddress;

    public Product(){
        this.name=null;
        this.productId=0;
        quantity=0;
        this.serviceAddress=null;
        this.cost = 0.0;
    }

    public Product(int productId, String name, int quantity, double cost, String serviceAddress) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.cost = cost;
        this.serviceAddress = serviceAddress;
    }
    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }
}
