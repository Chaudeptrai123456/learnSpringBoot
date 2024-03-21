package com.example.serverUser.Controller.Implement;

import com.example.serverUser.DTO.ProductDTO;
import com.example.serverUser.Entity.Product;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;

public interface ProductControllerImplement {

    ResponseEntity<Product> creatNewProduct(ProductDTO productDTO);

    ResponseEntity<Product> updateProduct(ProductDTO productDTO);

}
