package com.example.serverUser.Service.Product;

import com.example.serverUser.DTO.ProductDTO;
import com.example.serverUser.Entity.Feature;
import com.example.serverUser.Entity.Product;

import java.util.List;

public interface ProductService {

    List<Product> findAllProduct(String name);
    Product createProduct(ProductDTO productDTO);
    Product updateProduct(ProductDTO productDTO) throws Throwable;

    Product addFeatureToProduct(List<Feature> list);


}
