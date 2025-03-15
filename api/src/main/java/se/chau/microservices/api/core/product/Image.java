package se.chau.microservices.api.core.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Image {

    private String imageUrl;
    @JsonCreator  // Tells Jackson to use this constructor for deserialization
    public Image(@JsonProperty("imageUrl") String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
