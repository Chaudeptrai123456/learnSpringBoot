package se.chau.microservices.api.core.order;

public class ProductOrder {
    private int productId;
    private int quantity;

    public ProductOrder(){
        this.productId = 0;
        this.quantity = 0;
    }

    public ProductOrder(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
