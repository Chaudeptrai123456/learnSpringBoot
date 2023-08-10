package com.example.demo.Service;

import java.util.List;

import com.example.demo.DTO.ProductDTO;
import com.example.demo.Entity.Product;

public interface ProductService {
	public Product addNewProduct(ProductDTO dto);
	public Product updateProduct(ProductDTO dto);
	public Product getProductbyName(String name);
	public List<Product> getAllProduct();
	public Product getProductbyID(Long id);
	
}
