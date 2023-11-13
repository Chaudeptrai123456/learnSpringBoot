package com.example.Aiking.Entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="order")
public class Order_Items {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "orderId")
    private int orderId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderDetailId",referencedColumnName = "orderDetailId")
    private OrderDetails orderDetailId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="productId",referencedColumnName = "productId")
    private Product productId;
    @Column(name="quantity",nullable = false)
    private int quantity;
    @Column(name = "createDate",nullable = false)
    private Date createDate;
    @Column(name = "updateDate",nullable = false)
    private Date updateDate;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public OrderDetails getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(OrderDetails orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public Product getProductId() {
        return productId;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
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
