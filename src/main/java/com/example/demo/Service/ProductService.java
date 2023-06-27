package com.example.demo.Service;

import java.util.List;

import com.example.demo.Model.Product;

public interface ProductService {
	List<Product> findProductbyName(String name);
	Product findProductByID(Long id);
	Product createNewProduct(Product product);
    Product updateProduct(Product product,Long id);
 
//    Page<Product> getProductInPage(Integer pageNumber);
    List<Product> findAllProduct();
}
