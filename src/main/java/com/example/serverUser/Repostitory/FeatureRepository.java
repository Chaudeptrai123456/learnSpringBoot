package com.example.serverUser.Repostitory;

import com.example.serverUser.Entity.Feature;
import com.example.serverUser.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeatureRepository extends JpaRepository<Feature,Long> {
    @Query(value="SELECT * FROM product p INNER JOIN product_inventory pi ON p.productid = pi.productID WHERE pi.nameFeature=:nameFeature and pi.feature=:feature",nativeQuery = true)
    public Optional<Product> findProductByFeature(String nameFeature, String feature);

}
