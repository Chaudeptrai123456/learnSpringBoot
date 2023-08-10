package com.example.demo.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.ProductDTO;
import com.example.demo.Entity.Product;
import com.example.demo.Repository.ProductRepository;
@Service
public class ProductServiceImplement implements ProductService {
	@Autowired
	private ProductRepository repo;
	
	
	@Override
	public Product addNewProduct(ProductDTO dto) {
		Product newProduct = new Product();
		newProduct.setBrand(dto.getBrand());
		newProduct.setCost(dto.getCost());
		newProduct.setProductName(dto.getProductName());
		newProduct.setDescription(dto.getDescription());
		newProduct.setQuantity(dto.getQuantity());
		repo.save(newProduct);
		return newProduct;
	}

	@Override
	public Product updateProduct(ProductDTO dto) {
		Product newProduct = repo.getProductByName(dto.getProductName());
		newProduct.setBrand(dto.getBrand());
		newProduct.setCost(dto.getCost());
		newProduct.setProductName(dto.getProductName());
		newProduct.setDescription(dto.getDescription());
		newProduct.setQuantity(dto.getQuantity());
		repo.save(newProduct);
		return newProduct;
	}

	@Override
	public Product getProductbyName(String name) {
 		return repo.getProductByName(name);
	}

	@Override
	public List<Product> getAllProduct() {
 		return repo.findAll();
	}

	@Override
	public Product getProductbyID(Long id) {
		// TODO Auto-generated method stub
		return repo.getProductByID(id);
	}

}
