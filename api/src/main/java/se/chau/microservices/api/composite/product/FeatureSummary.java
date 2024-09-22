package se.chau.microservices.api.composite.product;

public class FeatureSummary {
    private int featureId;
    private String name;
    private String description;
    private int productId;
    public FeatureSummary(){
        this.featureId = 0;
        this.name = name;
        this.description = description;
        this.productId = productId;
    }
    public FeatureSummary(int id, String name, String description, int productId) {
        this.featureId = id;
        this.name = name;
        this.description = description;
        this.productId = productId;
    }


    public int getFeatureId() {
        return featureId;
    }

    public void setFeatureId(int featureId) {
        this.featureId = featureId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
