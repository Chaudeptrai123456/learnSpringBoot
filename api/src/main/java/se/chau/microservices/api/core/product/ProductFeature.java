package se.chau.microservices.api.core.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import se.chau.microservices.api.composite.product.FeatureSummary;
import se.chau.microservices.api.composite.product.ServiceAddress;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductFeature {
    private int productId;
    private String name;
    private int quantity;

    private double cost;
    private List<FeatureSummary> featureSummaryList;
    private ServiceAddress serviceAddresses;

    public ProductFeature() {
    }

    public ProductFeature(int productId, String name, int quantity,double cost, List<FeatureSummary> featureSummaryList) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.cost= cost;
        this.featureSummaryList = featureSummaryList;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public ServiceAddress getServiceAddresses() {
        return serviceAddresses;
    }

    public void setServiceAddresses(ServiceAddress serviceAddresses) {
        this.serviceAddresses = serviceAddresses;
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

    public List<FeatureSummary> getFeatureSummaryList() {
        return featureSummaryList;
    }

    public void setFeatureSummaryList(List<FeatureSummary> featureSummaryList) {
        this.featureSummaryList = featureSummaryList;
    }
}
