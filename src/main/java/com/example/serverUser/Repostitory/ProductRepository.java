package com.example.serverUser.Repostitory;

import com.example.serverUser.Entity.Inventory;
import com.example.serverUser.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query(value = "SELECT * from product p WHERE p.name_product=:productName",nativeQuery = true)
    Product findProductByName(String productName);

    @Query(value = "SELECT * from product p WHERE p.productID=:id",nativeQuery = true)
    Product findProductById(Long id);
//    @Query(value = "SELECT * FROM inventory i INNER JOIN ",nativeQuery = true)
//    Optional<Inventory> findAllInventoryOfProduct(Long id);
}
