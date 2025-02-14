package se.chau.microservices.api.core.order;

import java.time.LocalDate;
import java.util.List;

public class Order {

    private int userId;
    private int orderId;

    private String status;
    private LocalDate orderDate;
    private List<ProductOrder> products;
    private String serviceAddress;
    public Order(){
        this.userId=0;
        this.orderId=0;
        this.orderDate= LocalDate.now();
        this.serviceAddress=null;
        this.products = null;
    }

    public Order(int userId, int orderId , List<ProductOrder> products, String serviceAddress) {
        this.userId = userId;
        this.orderId = orderId;
        this.orderDate = LocalDate.now();
        this.products = products;
        this.serviceAddress = serviceAddress;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<ProductOrder> getProducts() {
        return products;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setProducts(List<ProductOrder> products) {
        this.products = products;
    }
}
