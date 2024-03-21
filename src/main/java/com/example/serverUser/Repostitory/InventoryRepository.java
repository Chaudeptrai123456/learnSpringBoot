package com.example.serverUser.Repostitory;

import com.example.serverUser.Entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {
    @Query(value = "SELECT * FROM inventory i WHERE i.nameInventory LIKE '%:name%'",nativeQuery = true)
    Optional<Inventory> findInventory(String name);
    @Query(value = "SELECT * FROM inventory i WHERE i.inventoryid=:id",nativeQuery = true)
    Inventory findInventorybyId(Long id);
}
