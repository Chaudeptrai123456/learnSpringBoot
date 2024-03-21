package com.example.serverUser.Controller.Implement;

import com.example.serverUser.DTO.InventoryDTO;
import com.example.serverUser.DTO.Product_InventoryDTO;
import com.example.serverUser.Entity.Inventory;
import com.example.serverUser.Entity.Product_Inventory;
import org.springframework.http.ResponseEntity;

public interface InventoryControllerImplement {

    ResponseEntity<Inventory> createInventory(InventoryDTO inventoryDTO);

    ResponseEntity<Product_Inventory> addNewProductToInventory(Product_InventoryDTO inventoryDTO);

    ResponseEntity<Product_Inventory> updateProductToInventory(Product_InventoryDTO inventoryDTO);

}
