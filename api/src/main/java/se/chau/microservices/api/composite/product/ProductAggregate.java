package se.chau.microservices.api.composite.product;

import java.util.List;

public class ProductAggregate {
    private int productId;
    private String name;
    private int weight;

    private List<ReviewSummary> reviewSummaryList;
    private List<RecommendationSummary> recommendationSummaryList;
    public ProductAggregate() {
        this.productId = 1;
        this.name = "test";
        this.weight = 123;
        this.reviewSummaryList = null;
        this.recommendationSummaryList = null;
        this.serviceAddresses = null;
    }
    public ProductAggregate(int productId, String name, int weight, List<ReviewSummary> reviewSummaryList, List<RecommendationSummary> recommendationSummaryList, ServiceAddress serviceAddresses) {
        this.productId = productId;
        this.name = name;
        this.weight = weight;
        this.reviewSummaryList = reviewSummaryList;
        this.recommendationSummaryList = recommendationSummaryList;
        this.serviceAddresses = serviceAddresses;
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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
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
}
