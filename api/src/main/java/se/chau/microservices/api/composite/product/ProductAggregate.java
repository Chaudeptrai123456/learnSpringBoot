package se.chau.microservices.api.composite.product;

import se.chau.microservices.api.core.product.Image;

import java.util.List;

public class ProductAggregate {
    private int productId;
    private String name;
    private int quantity;
    private double cost;
    private List<Image> imageList;
    private List<ReviewSummary> reviewSummaryList;
    private List<RecommendationSummary> recommendationSummaryList;
    private List<FeatureSummary> featureSummaryList;

    private List<DisCountSummary> disCountSummaries;

    public List<DisCountSummary> getDisCountSummaries() {
        return disCountSummaries;
    }

    public void setDisCountSummaries(List<DisCountSummary> disCountSummaries) {
        this.disCountSummaries = disCountSummaries;
    }

    public ProductAggregate() {
        this.productId = 1;
        this.name = "test";
        this.quantity = 123;
        this.reviewSummaryList = null;
        this.recommendationSummaryList = null;
        this.serviceAddresses = null;
        this.disCountSummaries = null;
        this.cost = 0.0;
    }
    public ProductAggregate(int productId, String name, int quantity, double cost,List<ReviewSummary> reviewSummaryList, List<RecommendationSummary> recommendationSummaryList,List<FeatureSummary> featureSummaryList,List<DisCountSummary> disCountSummaries,List<Image> imageList ,ServiceAddress serviceAddresses) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.reviewSummaryList = reviewSummaryList;
        this.recommendationSummaryList = recommendationSummaryList;
        this.serviceAddresses = serviceAddresses;;
        this.featureSummaryList = featureSummaryList;
        this.disCountSummaries = disCountSummaries;
        this.cost = cost;
        this.imageList = imageList;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
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
