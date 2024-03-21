package com.example.serverUser.Controller;

import com.example.serverUser.Controller.Implement.ProductControllerImplement;
import com.example.serverUser.DTO.ProductDTO;
import com.example.serverUser.Entity.Product;
import com.example.serverUser.Service.AdminService;
import com.example.serverUser.Service.Inventory.InventoryServiceImplement;
import com.example.serverUser.Service.Product.ProductServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private ProductServiceImplement productServiceImplement;
    @Autowired
    private InventoryServiceImplement inventoryServiceImplement;
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public ResponseEntity<Product> creatNewProduct(ProductDTO productDTO) {
        System.out.println("test " + productDTO.getCost());
        Product body = this.productServiceImplement.createProduct(productDTO);
        return new ResponseEntity<Product>(body,HttpStatusCode.valueOf(200));
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ResponseEntity<Product> updateProduct(ProductDTO productDTO) {
        return null;
    }
    @RequestMapping(value="/getAll",method= RequestMethod.GET)
    public ResponseEntity<List<Product>> getAllProduct() {
        List<Product> list = this.productServiceImplement.findAllProduct("test");
        return new ResponseEntity<List<Product>>(list, HttpStatusCode.valueOf(200));
    }
}
