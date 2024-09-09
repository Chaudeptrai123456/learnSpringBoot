package se.chau.microservices.core.product.Persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<ProductEntity,String>, CrudRepository<ProductEntity,String> {
    Optional<ProductEntity> findByProductId(int productId);
}
