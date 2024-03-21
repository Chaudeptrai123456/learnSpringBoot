package com.example.serverUser.Service.Inventory;

import com.example.serverUser.DTO.InventoryDTO;
import com.example.serverUser.DTO.Product_InventoryDTO;
import com.example.serverUser.Entity.Inventory;
import com.example.serverUser.Entity.Product;
import com.example.serverUser.Entity.Product_Inventory;
import com.example.serverUser.Error.ErrorResponse;
import com.example.serverUser.Repostitory.InventoryRepository;
import com.example.serverUser.Repostitory.ProductRepository;
import com.example.serverUser.Repostitory.Product_InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class InventoryServiceImplement implements InventoryService{
    @Autowired
    private Product_InventoryRepository productInventory;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;
    public Product_Inventory addProducttoInventory(Product_InventoryDTO productInventoryDTO) throws ErrorResponse {
            Product_Inventory pi = new Product_Inventory();
            Product product = productRepository.findProductById(productInventoryDTO.getProductID());
            Inventory inventory = inventoryRepository.findInventorybyId(productInventoryDTO.getInventoryID());
            pi.setProduct(product);
            pi.setDateUpdate(new Date());
            pi.setDateCreate(new Date());
            pi.setQuanlity(productInventoryDTO.getQuanlity());
            pi.setInventory(inventory);
            List<Product_Inventory> asd = productInventory.findAllPI();
            for (Product_Inventory index : asd) {
                System.out.println("index " + index.getPi_id());
            }
            try {
            productInventory.save(pi);
            System.out.println(pi.getInventory().getNameInventory());
            product.addProduct_InventoryToList(pi);
            productRepository.save(product);
            return pi;
        } catch(Error error) {
            throw  new ErrorResponse("san pham ko the them vao kho");
        }

    }
    @Override
    public Inventory createnewInventory(InventoryDTO inventoryDTO) {
        Inventory newInventory = new Inventory();
        newInventory.setNameInventory(inventoryDTO.getNameInventory());
        newInventory.setAddress(inventoryDTO.getAddress());
        this.inventoryRepository.save(newInventory);
        return newInventory;
    }

    @Override
    public List<Inventory> getAllInventory() {
        List<Inventory> result = this.inventoryRepository.findAll();
        return result;
    }

    @Override
    public List<Product_Inventory> getAllProductFromInventory(Long id) {
        List<Product_Inventory> list = this.productInventory.getAllProductFromInventory(id);
        return list;
    }

    @Override
    public Product_Inventory updateProductInInventory(Product_InventoryDTO productInventoryDTO) {
        Product_Inventory pi = this.productInventory.findByInventorynameandProductname(productInventoryDTO.getInventoryID(), productInventoryDTO.getProductID());
        pi.setQuanlity(productInventoryDTO.getQuanlity());
        pi.setDateUpdate(new Date());
        this.productInventory.save(pi);
        return pi;
    }

    public List<Product_Inventory> getAllProductFromInventory1(Long id) {
        List<Product_Inventory> list = this.productInventory.getAllProductFromInventory(id);
        return list;
    }
}
