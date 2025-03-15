package se.chau.microservices.core.product.Persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "image")
public class ImageEntity {
    @Id
    private String id;
    private String imageUrl;
    private int productId;

    public int getProductId() {
        return productId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }



    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
