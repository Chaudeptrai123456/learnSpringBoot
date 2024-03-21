package com.example.serverUser.Repostitory;

import com.example.serverUser.Entity.Product;
import com.example.serverUser.Entity.Product_Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Product_InventoryRepository extends JpaRepository<Product_Inventory,Long> {
    @Query(value = "SELECT * FROM product_inventory pi WHERE pi.product_id =:product AND pi.inventory_id =:inventory",nativeQuery = true)
    Product_Inventory findByInventorynameandProductname(Long inventory,Long product);
    @Query(value = "SELECT * FROM product_inventory",nativeQuery = true)
    List<Product_Inventory> findAllPI();
//    @Query(value="SELECT * FROM product p INNER JOIN product_inventory pi WHERE p.productid = pi.product_id && pi.inventory_id = :id",nativeQuery = true)
    @Query(value = "SELECT * FROM product p INNER JOIN product_inventory pi WHERE p.productid = pi.product_id && pi.inventory_id = :id",nativeQuery = true)
    List<Product_Inventory> getAllProductFromInventory(Long id);


}
