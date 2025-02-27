package se.chau.microservices.api.composite.product;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class DisCountSummary {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private Double value;
    private String description;

    public DisCountSummary(){}

    public DisCountSummary(LocalDate startDate, LocalDate endDate, Double value, String description) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.value = value;
        this.description = description;
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
}
