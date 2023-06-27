package com.example.demo.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.Model.Product;
import com.example.demo.Repository.ProductRepository;

@Service
public class ProductServiceImplement implements ProductService {

	@Autowired
	private ProductRepository repo;
	
	@Override
	public List<Product> findProductbyName(String name) {
		String temp = "%"+name+"%";
		return repo.findProductsbyName(temp);
	}

	@Override
	public Product findProductByID(Long id) {
		return repo.findById(id).get();
	}

	@Override
	public Product createNewProduct(Product product) {
		return repo.save(product);
	}

	@Override
	public Product updateProduct(Product product, Long id) {
		 Product result = repo.findById(id).get();
	     result.setProductName(product.getProductName());
	     result.setProductCost(product.getProductCost());
	     repo.save(result);
	     return result;
	}

	@Override
	public List<Product> findAllProduct() {
		return (List<Product>) repo.findAll();
	}

	public List<Product> getProductpage(int pageNo) {
		 int pageSize = 10;
	     Pageable pageable = PageRequest.of(0,5);
	     Page<Product> page = repo.findAll(pageable);
	     List<Product> products = page.getContent();
	     return products;		 
	}
	 
}
