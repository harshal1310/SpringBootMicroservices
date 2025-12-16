package com.springProject.Service;

import com.springProject.Pojo.ProductEntity;
import com.springProject.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public String updateStock(String itemName, int quantity) {
        Optional<ProductEntity> item = productRepository.findByItemNameIgnoreCase(itemName);
        if (item.isPresent()) {
            ProductEntity inventoryItem = item.get();
            inventoryItem.setQuantity(inventoryItem.getQuantity() - quantity);
            productRepository.save(inventoryItem);
            return "Stock updated";
        } else {
            return "Item not found";
        }
    }
    public String addItem(String itemName, int quantity) {
        System.out.println("name is" + itemName);
        System.out.println("quantity is" + quantity);
        if (productRepository.findByItemNameIgnoreCase(itemName).isPresent()) {  // Fix: Use Optional check
            return "Item already exists!";
        }

        ProductEntity product = new ProductEntity();  // Fix: Use correct class name
        product.setName(itemName);  // Fix: Use correct setter
        product.setQuantity(quantity);
        product.setPrice(1.00);
        productRepository.save(product);
        return "Item added successfully!";
    }
public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }
    public boolean checkStock(String itemName, int quantity) {
        Optional<ProductEntity> item = productRepository.findByItemNameIgnoreCase(itemName);
        if (item.isPresent()) {
            return item.get().getQuantity() >= quantity;
        }
        return false;
    }
}
