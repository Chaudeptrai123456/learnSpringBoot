package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Model.Product;
import com.example.demo.Service.ProductServiceImplement;

@RestController
@RequestMapping("/product")
public class productController {
	
	@Autowired
	private ProductServiceImplement service;
	 @GetMapping("/name/{name}")
	public ResponseEntity<List<Product>> handleFindProductByname(@PathVariable(required = true) String name){
		List<Product> list = service.findProductbyName(name);
		HttpHeaders header = new HttpHeaders();
		header.add("functionality", "find product by name");
		return new ResponseEntity<>(list,header,HttpStatusCode.valueOf(200));
	}
	 @GetMapping("/page/{pageNo}")
	 public ResponseEntity<?> handlePage(@PathVariable(required=true) int pageNo){
		 List<Product> list = service.getProductpage(pageNo);
		 return new ResponseEntity<>(list,HttpStatus.ACCEPTED);
	 }
	 @GetMapping("/create")
	    public ResponseEntity<Product> handleCreateProduct(@RequestBody Product product) {
	        Product temp = new Product();
	        temp.setProductName(product.getProductName());
	        temp.setProductCost(product.getProductCost());
	        temp.setproductQuantity(product.getproductQuantity());
	        service.createNewProduct(temp);
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("functionality","create new product");
	        return new ResponseEntity<Product>(product,headers,HttpStatusCode.valueOf(200));
	    }
	    @GetMapping("/all")
	    public ResponseEntity<List<Product>> handleGetAllProduct(){
	        List<Product> result = service.findAllProduct();
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("functionality","get all products");
	        return new ResponseEntity<>(result,headers,HttpStatusCode.valueOf(200));
	    }
	    

	    @PostMapping("/update/{id}")
	    public ResponseEntity<Product> handleUpdate(@RequestBody Product product,@PathVariable Long id) {
	        Product result = service.updateProduct(product,id);
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("functionality","update product by id");
	        return new ResponseEntity<Product>(result,headers,HttpStatusCode.valueOf(200));
	    }
	
}
