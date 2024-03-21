package com.example.serverUser.Service.Inventory;

import com.example.serverUser.DTO.InventoryDTO;
import com.example.serverUser.DTO.Product_InventoryDTO;
import com.example.serverUser.Entity.Inventory;
import com.example.serverUser.Entity.Product;
import com.example.serverUser.Entity.Product_Inventory;

import java.util.List;

public interface InventoryService {

    Product_Inventory addProducttoInventory(Product_InventoryDTO productInventoryDTO);

    Inventory createnewInventory(InventoryDTO inventoryDTO);
    List<Inventory> getAllInventory();

    List<Product_Inventory> getAllProductFromInventory(Long id);

    Product_Inventory updateProductInInventory(Product_InventoryDTO productInventoryDTO);

}
