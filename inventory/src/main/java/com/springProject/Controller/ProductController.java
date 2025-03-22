package com.springProject.Controller;

import com.springProject.Pojo.Product;
import com.springProject.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/update-stock")
   // public String updateStock(@RequestParam String itemName, @RequestParam int quantity) {
    public String updateStock(@RequestBody Product request) {

        return productService.updateStock(request.getItemName(), request.getQuantity());
    }


    @PostMapping("/add")
    public ResponseEntity<String> addItem(@RequestBody Product request) {
        System.out.println(request.getItemName());
System.out.println(request.toString());
        String response = productService.addItem(request.getItemName(), request.getQuantity());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/check-stock")
    public boolean checkStock(@RequestParam String itemName, @RequestParam int quantity) {
        return productService.checkStock(itemName, quantity);
    }

}
