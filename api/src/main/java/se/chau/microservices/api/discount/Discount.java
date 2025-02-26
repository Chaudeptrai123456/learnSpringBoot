package se.chau.microservices.api.discount;

import java.time.LocalDate;

public class Discount {
    private int productId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double value;
    private String serviceAddress;

    private String description;
    public Discount() {
        this.description = "";
        this.productId = 0;
        this.startDate = LocalDate.now();
        this.endDate = LocalDate.now();
        this.value = 0.0;
    }

    public Discount(int productId, LocalDate start, LocalDate end, Double value, String serviceAddress, String decription) {
        this.productId = productId;
        this.startDate = start;
        this.endDate = end;
        this.value = value;
        this.serviceAddress = serviceAddress;
        this.description = decription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public LocalDate getStart() {
        return startDate;
    }

    public void setStart(LocalDate start) {
        this.startDate = start;
    }

    public LocalDate getEnd() {
        return endDate;
    }

    public void setEnd(LocalDate end) {
        this.endDate = end;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
