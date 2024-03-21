package com.example.serverUser.Service;

import com.example.serverUser.Entity.Feature;
import com.example.serverUser.Entity.Inventory;
import com.example.serverUser.Entity.Product;
import com.example.serverUser.Entity.Product_Inventory;
import com.example.serverUser.Repostitory.FeatureRepository;
import com.example.serverUser.Repostitory.InventoryRepository;
import com.example.serverUser.Repostitory.ProductRepository;
import com.example.serverUser.Repostitory.Product_InventoryRepository;
import com.example.serverUser.Service.FindProduct.FindProductImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

@Service
public class AdminService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private FeatureRepository featureRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private Product_InventoryRepository productInventoryRepository;

    @Autowired
    private FindProductImplement findProductImplement;
    public List<Product_Inventory> test() {
        try {
        Product newProduct = new Product();
        newProduct.setCost(121231.1D);
        newProduct.setNameProduct("product_test_slave 5123a@sdas2");
        Inventory inventory = new Inventory();
        inventory.setNameInventory("inventory qwewq");
        inventory.setAddress("ho chi minh vietnam quan112asqweqwdsa3123");

        Feature newFeature = new Feature();
        newFeature.setFeature("light pink 12asdadasd@sa1");
        newFeature.setNameFeature("color desktop 222asdsasdsad2");
        featureRepository.save(newFeature);
        Product_Inventory pi = new Product_Inventory();
        pi.setProduct(newProduct);
        pi.setQuanlity(10);
        pi.setInventory(inventory);
        pi.setDateCreate(new Date());
        pi.setDateUpdate(new Date());
        Set<Product_Inventory> test = new HashSet<>();
        test.add(pi);
        newProduct.setListInventory(test);
        inventoryRepository.save(inventory);
        productRepository.save(newProduct);
        productInventoryRepository.save(pi);

        List<Product> p = productRepository.findAll();
        List<Inventory> a = inventoryRepository.findAll();
        return productInventoryRepository.findAllPI();
        } catch(Exception ex)
        {
            throw  ex;
        }
    }



}
