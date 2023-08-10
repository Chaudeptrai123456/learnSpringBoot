package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.Entity.Product;

public interface ProductRepository extends JpaRepository<Product,Long>{
	@Query(value="SELECT * FROM product p WHERE p.productname = :name",nativeQuery=true)
	public Product getProductByName(String name);
	
	@Query(value="SELECT * FROM product p WHERE p.productid = :productid",nativeQuery=true)
	public Product getProductByID(Long productid);

}
