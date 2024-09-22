package se.chau.microservices.core.invetory.Persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "inventory")
public class InventoryEntity {
    @Id
    private String id;
    @Version
    private Integer version;
    @Indexed(unique = true)
    private int inventoryId;
    private int quantity;
    private double cost;



}
