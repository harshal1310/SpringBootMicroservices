package com.springProject.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springProject.Kafka.KafkaProducer;
//import com.springProject.Pojo.Product;
import com.springProject.Pojo.ProductEntity;
import com.springProject.dto.Product;
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

        return productService.updateStock((String) request.getItemName(), request.getQuantity());
    }


    @PostMapping("/add")
    public ResponseEntity<String> addItem(@RequestBody Product request) {
        System.out.println(request.getItemName());
System.out.println(request.toString());
        String response = productService.addItem((String) request.getItemName(), request.getQuantity());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public List<ProductEntity> getAllProducts() {
        System.out.println("in all products");
        List<ProductEntity> productsFromDb = productService.getAllProducts();

        for(int i =0 ; i<100; i++) {
            //int partition=i%3;
            System.out.println("counter- " + i);
            Product avroProduct = Product.newBuilder()
                    .setItemName("Product " + i)
                    .setPrice((double) i)
                    .setQuantity(i)
                    .setMsg("Product " + i)
                    .build();

            try {
                //String message = objectMapper.writeValueAsString(product);
                kafkaProducer.sendMessage("topic-1", 0, avroProduct);
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
