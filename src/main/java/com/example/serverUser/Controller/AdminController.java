package com.example.serverUser.Controller;

import com.example.serverUser.DTO.ProductDTO;
import com.example.serverUser.DTO.Product_InventoryDTO;
import com.example.serverUser.Entity.Inventory;
import com.example.serverUser.Entity.Product;
import com.example.serverUser.Entity.Product_Inventory;
import com.example.serverUser.Error.ErrorResponse;
import com.example.serverUser.Service.AdminService;
import com.example.serverUser.Service.Inventory.InventoryServiceImplement;
import com.example.serverUser.Service.Product.ProductServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private ProductServiceImplement productServiceImplement;
    @Autowired
    private InventoryServiceImplement inventoryServiceImplement;
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/test")
    public ResponseEntity<List<Product_Inventory>> test() {
        List<Product_Inventory> q =  adminService.test();
        System.out.println(q);
        return new ResponseEntity<List<Product_Inventory>>(q, HttpStatus.ACCEPTED);
    }
//    @GetMapping("/createInventoryPro")
//    public Product_Inventory createProduct_Inventory(ProductDTO productDTO) throws ErrorResponse{
//        try {
////            Product_Inventory pi1 = inventoryServiceImplement.addProducttoInventory(productDTO);
//            return null;
//        }catch (Exception exception) {
//            throw new ErrorResponse(exception.getMessage());
//        }
//
//    }
//    @GetMapping("/getAllInventory")
//    public List<Inventory> getAllInventory(){
//        List<Inventory> list = inventoryServiceImplement.getAllInventory();
//        return list;
//    }
//    @GetMapping("/createProduct")
//    public Product createProduct(ProductDTO productDTO) throws ErrorResponse{
//        try {
//            Product pro =  productServiceImplement.createProduct(productDTO);
//            return pro;
//        }catch (Exception exception) {
//            System.out.println(exception.getMessage());
//            throw new ErrorResponse(exception.getMessage());
//        }
//
//    }
//    @GetMapping("/updateProduct")
//    public Product updateProduct(ProductDTO productDTO) throws ErrorResponse{
//        try {
//            System.out.println(productDTO.getNameProduct()+" " + productDTO.getCost());
//            Product pro =  productServiceImplement.updateProduct(productDTO);
//            List<Product> lsit = productServiceImplement.findAllProduct("test");
//            for (Product index : lsit) {
//                System.out.println(index.getProductID()+" " + index.getNameProduct());
//            }
//            return pro;
//        }catch (Exception exception) {
//            System.out.println(exception.getMessage());
//            throw new ErrorResponse(exception.getMessage());
//        }
//
//    }
//    @GetMapping("/getAll")
//    public List<Product> getAllProduct(){
//        List<Product> products  = productServiceImplement.findAllProduct("test");
//        return products;
//    }
}
