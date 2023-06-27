package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.example.demo.Model.Product;

@Service
public interface ProductRepository extends CrudRepository<Product,Long> {
	
	@Query(value="SELECT * FROM product p WHERE p.productname like :name",nativeQuery = true)
	List<Product> findProductsbyName(String name);	
	@Query(value="SELECT * FROM product p WHERE p.productname = :name",nativeQuery = true)
	Product findProductByName(String name);
	
    Page<Product> findAll(Pageable pageable);

}
