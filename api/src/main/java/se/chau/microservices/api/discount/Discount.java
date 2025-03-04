package se.chau.microservices.api.discount;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class Discount {
    private int productId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private Double value;
    private String description;
    private String serviceAddress;

    public Discount() {
    }

    public Discount(int productId, LocalDate startDate, LocalDate endDate, Double value, String serviceAddress, String description) {
        this.productId = productId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.value = value;
        this.serviceAddress = serviceAddress;
        this.description = description;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
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
}