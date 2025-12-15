package com.springProject.Client;

import com.springProject.Pojo.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "inventory-service", url = "http://localhost:8080/api/products")
public interface InventoryClient {
    @GetMapping("/check-stock")
    ResponseEntity checkStock(@RequestParam String itemName, @RequestParam int quantity);

    @PostMapping("/update-stock")
    String updateStock(@RequestBody Map<String, Object> request);
}
