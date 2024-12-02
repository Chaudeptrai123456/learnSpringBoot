package se.chau.microservices.api.composite.product;

import java.util.List;

public class ProductAggregate {
    private int productId;
    private String name;
    private int quantity;
    private double cost;
    private List<ReviewSummary> reviewSummaryList;
    private List<RecommendationSummary> recommendationSummaryList;
    private List<FeatureSummary> featureSummaryList;
    public ProductAggregate() {
        this.productId = 1;
        this.name = "test";
        this.quantity = 123;
        this.reviewSummaryList = null;
        this.recommendationSummaryList = null;
        this.serviceAddresses = null;
        this.cost = 0.0;
    }
    public ProductAggregate(int productId, String name, int quantity, double cost,List<ReviewSummary> reviewSummaryList, List<RecommendationSummary> recommendationSummaryList,List<FeatureSummary> featureSummaryList ,ServiceAddress serviceAddresses) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.reviewSummaryList = reviewSummaryList;
        this.recommendationSummaryList = recommendationSummaryList;
        this.serviceAddresses = serviceAddresses;;
        this.featureSummaryList = featureSummaryList;
        this.cost = cost;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    private ServiceAddress serviceAddresses;

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

    public List<ReviewSummary> getReviewSummaryList() {
        return reviewSummaryList;
    }

    public void setReviewSummaryList(List<ReviewSummary> reviewSummaryList) {
        this.reviewSummaryList = reviewSummaryList;
    }

    public List<RecommendationSummary> getRecommendationSummaryList() {
        return recommendationSummaryList;
    }

    public void setRecommendationSummaryList(List<RecommendationSummary> recommendationSummaryList) {
        this.recommendationSummaryList = recommendationSummaryList;
    }



    public ServiceAddress getServiceAddresses() {
        return serviceAddresses;
    }

    public void setServiceAddresses(ServiceAddress serviceAddresses) {
        this.serviceAddresses = serviceAddresses;
    }

    public List<FeatureSummary> getFeatureSummaryList() {
        return featureSummaryList;
    }

    public void setFeatureSummaryList(List<FeatureSummary> featureSummaryList) {
        this.featureSummaryList = featureSummaryList;
    }
}
