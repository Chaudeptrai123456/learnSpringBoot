package com.example.serverUser.Controller;

import com.example.serverUser.Controller.Implement.InventoryControllerImplement;
import com.example.serverUser.DTO.InventoryDTO;
import com.example.serverUser.DTO.Product_InventoryDTO;
import com.example.serverUser.Entity.Inventory;
import com.example.serverUser.Entity.Product;
import com.example.serverUser.Entity.Product_Inventory;
import com.example.serverUser.Service.AdminService;
import com.example.serverUser.Service.Inventory.InventoryServiceImplement;
import com.example.serverUser.Service.Product.ProductServiceImplement;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private ProductServiceImplement productServiceImplement;
    @Autowired
    private InventoryServiceImplement inventoryServiceImplement;

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public ResponseEntity<Inventory> createInventory(@RequestBody InventoryDTO inventoryDTO) {
        Inventory result = this.inventoryServiceImplement.createnewInventory(inventoryDTO);
        return new ResponseEntity<Inventory>(result, HttpStatusCode.valueOf(200));
    }
    @RequestMapping(value="/getAll",method = RequestMethod.GET)
    public ResponseEntity<List<Inventory>> getAllInventory() {
        List<Inventory> list = this.inventoryServiceImplement.getAllInventory();
        return new ResponseEntity<List<Inventory>>(list,HttpStatusCode.valueOf(200));
    }
    @RequestMapping(value="/getAllProductFromInventory",method = RequestMethod.GET)
    public ResponseEntity<List<Product_Inventory>> getAllProductFromInventory(
//            @RequestBody Long id
    ) {
        List<Product_Inventory> list = this.inventoryServiceImplement.getAllProductFromInventory(52L);
        return new ResponseEntity<>(list,HttpStatusCode.valueOf(200));
    }
    @RequestMapping(value="/addNewProductToInventory",method = RequestMethod.POST)
    public ResponseEntity<Product_Inventory> addNewProductToInventory(Product_InventoryDTO productInventoryDTO) {
        Product_Inventory list = this.inventoryServiceImplement.addProducttoInventory(productInventoryDTO);
        return new ResponseEntity<Product_Inventory>(list,HttpStatusCode.valueOf(200));
    }
    @RequestMapping(value="/updateProductInInventory",method = RequestMethod.POST)
    public ResponseEntity<Product_Inventory> udpateProductInInventory(@RequestBody Product_InventoryDTO productInventoryDTO) {
        Product_Inventory body = this.inventoryServiceImplement.updateProductInInventory(productInventoryDTO);
        return new ResponseEntity<Product_Inventory>(body,HttpStatusCode.valueOf(200));
    }
    public ResponseEntity<Product_Inventory> updateProductToInventory(Product_InventoryDTO inventoryDTO) {
        return null;
    }
}
