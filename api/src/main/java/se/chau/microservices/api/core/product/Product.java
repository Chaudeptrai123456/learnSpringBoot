package se.chau.microservices.api.core.product;

import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

public class Product {
    private int productId;
    private String name;

    private int weight;

    private String serviceAddress;

    public Product(){
        this.name=null;
        this.productId=0;
        weight=0;
        this.serviceAddress=null;
    }



    public Product(int productId, String name, int weight, String serviceAddress) {
        this.productId = productId;
        this.name = name;
        this.weight = weight;
        this.serviceAddress = serviceAddress;
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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }
}
