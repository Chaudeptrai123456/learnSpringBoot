package se.chau.microservices.core.invetory.Persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.awt.*;

@Repository
public interface InventoryRepository extends ReactiveCrudRepository<InventoryEntity,Integer> {
}
