package se.chau.microservices.api.core.Feature;

public class Feature {
    private int featureId;
    private String name;
    private String description;

    private int productId;
    private String serviceAddress;
    public Feature(){
        this.featureId = 0;
        this.productId = 0;
        this.name = "";
        this.description = "";

    }

    public Feature(int featureId, int productId, String name, String description, String serviceAddress) {
        this.featureId = featureId;
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.serviceAddress = serviceAddress;
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

    public int getFeatureId() {
        return featureId;
    }

    public void setFeatureId(int featureId) {
        this.featureId = featureId;
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

}
