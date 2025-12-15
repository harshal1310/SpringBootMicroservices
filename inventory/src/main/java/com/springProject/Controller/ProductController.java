package com.springProject.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springProject.Kafka.KafkaProducer;
import com.springProject.Pojo.Product;
import com.springProject.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    KafkaProducer kafkaProducer;

    private final ObjectMapper objectMapper = new ObjectMapper();


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
        System.out.println("in all products");
        for(int i =0 ; i<100; i++) {
            //int partition=i%3;
            System.out.println("counter- " + i);
            Product product = new Product();
            product.setName("Product " + i);
            product.setQuantity(i);
            product.setPrice(i);
            product.setMsg("Product " + i);
            try {
                String message = objectMapper.writeValueAsString(product);
                kafkaProducer.sendMessage("topic-1", 0, message);
            } catch (Exception e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
        return productService.getAllProducts();
    }

    @GetMapping("/check-stock")
    public ResponseEntity<?> checkStock(@RequestParam String itemName, @RequestParam int quantity) {
        if(itemName == null  || itemName.isEmpty() || quantity <= 0) {
            return new ResponseEntity<>("Either itemname is empty or quantity is less than 1", HttpStatus.BAD_REQUEST);
        }
        boolean isExists =  productService.checkStock(itemName, quantity);
        if(isExists) {
            return new ResponseEntity<>("Product is  exists", HttpStatus.OK);
        }
        return new ResponseEntity<>("Product is  not exists", HttpStatus.BAD_REQUEST);
    }

}
