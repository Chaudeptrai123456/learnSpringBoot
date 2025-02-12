package se.chau.microservices.api.core.product;

public class ProductUpdate  {

    private int quantity;
    private double cost;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quanlity) {
        this.quantity = quanlity;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
