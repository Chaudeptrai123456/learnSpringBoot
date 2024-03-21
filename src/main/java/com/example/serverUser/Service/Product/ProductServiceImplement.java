package com.example.serverUser.Service.Product;

import com.example.serverUser.DTO.ProductDTO;
import com.example.serverUser.Entity.Feature;
import com.example.serverUser.Entity.Inventory;
import com.example.serverUser.Entity.Product;
import com.example.serverUser.Entity.Product_Inventory;
import com.example.serverUser.Repostitory.FeatureRepository;
import com.example.serverUser.Repostitory.InventoryRepository;
import com.example.serverUser.Repostitory.ProductRepository;
import com.example.serverUser.Repostitory.Product_InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
@Service
public class ProductServiceImplement implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private FeatureRepository featureRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private Product_InventoryRepository productInventoryRepository;

    @Override
    public List<Product> findAllProduct(String name) {
        List<Product> list = productRepository.findAll();
        return list;
    }

    @Override
    public Product createProduct(ProductDTO productDTO) {
        Product newProduct = new Product();
        newProduct.setNameProduct(productDTO.getNameProduct());
        newProduct.setCost(productDTO.getCost());
        productRepository.save(newProduct);
        Product_Inventory product_inventory = new Product_Inventory();
        product_inventory.setProduct(newProduct);
        product_inventory.setQuanlity(productDTO.getQuantity());
        product_inventory.setDateCreate(new Date());
        product_inventory.setDateUpdate(new Date());
        Inventory inventory = inventoryRepository.findInventorybyId(productDTO.getInventoryID());
        System.out.println(inventory.getNameInventory());
        product_inventory.setInventory(inventory);
        productInventoryRepository.save(product_inventory);
       return newProduct;
    }

    @Override
    public Product updateProduct(ProductDTO productDTO)  {
        Product newProduct =  productRepository.findProductByName(productDTO.getNameProduct());
        System.out.println(productDTO.getInventoryID()  + newProduct.getNameProduct());
        newProduct.setNameProduct(productDTO.getNameProduct());
        newProduct.setCost(productDTO.getCost());
        Product_Inventory pi = productInventoryRepository.findByInventorynameandProductname(productDTO.getInventoryID(), newProduct.getProductID());
        pi.setQuanlity(productDTO.getQuantity());
        productRepository.save(newProduct);
        productInventoryRepository.save(pi);
        return newProduct;

    }

    public List<Product_Inventory> getAllPIP() {
        List<Product_Inventory> list = this.productInventoryRepository.findAll();
        return list;
    }
    @Override
    public Product addFeatureToProduct(List<Feature> list) {
        return null;
    }


}
