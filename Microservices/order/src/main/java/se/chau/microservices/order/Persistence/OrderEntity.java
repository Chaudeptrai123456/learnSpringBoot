package se.chau.microservices.order.Persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import se.chau.microservices.api.core.order.ProductOrder;
import se.chau.microservices.api.core.product.Product;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "orders")
public class OrderEntity {
    @Id
    private String id;

    private int orderId;
    private OrderStatus status;
    private int userId;
    private LocalDate orderDate;

    private List<ProductOrder> products;

    public OrderEntity(LocalDate orderDate,List<ProductOrder> productOrderList) {
        this.orderDate = orderDate;
        this.status = OrderStatus.CREATE;
        this.products = productOrderList==null ? new ArrayList<>() : productOrderList ;
    }

    public OrderEntity() {
        this.status = OrderStatus.CREATE;
        this.products = new ArrayList<>();
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    // Getters and Setters
    public int getId() {
        return orderId;
    }

    public void setId(int id) {
        this.orderId = id;
    }

    public int getCustomerName() {
        return userId;
    }

    public void setCustomerName(int userId) {
        this.userId = userId;
    }

    public List<ProductOrder> getProducts() {
        return products;
    }

    public void setProducts(List<ProductOrder> products) {
        this.products = products;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void addProductToOrder(Product product) {
        this.products.add(new ProductOrder(product.getProductId(),product.getQuantity()));
    }

}
