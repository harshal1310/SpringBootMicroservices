package com.springProject.Service;

import com.springProject.Client.InventoryClient;
import com.springProject.Pojo.Order;
import com.springProject.Repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrderService {
OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
public OrderService(OrderRepository OrderRepository,InventoryClient inventoryClient) {
    this.orderRepository = OrderRepository;
    this.inventoryClient = inventoryClient;
    }


    @Transactional
    public String placeOrder(Order order) {

        System.out.println("order name:" + order.getItemName());
        System.out.println("order quantity:" + order.getQuantity());
        ResponseEntity isStockAvailable = inventoryClient.checkStock(order.getItemName(), order.getQuantity());

        if(!isStockAvailable.getStatusCode().is2xxSuccessful()) {
            return "Order failed! Not enough stock.";

        }

        orderRepository.save(order);


        Map<String, Object> request = new HashMap<>();
        request.put("itemName", order.getItemName());
        request.put("quantity", order.getQuantity());
       // if(true)
       // throw new RuntimeException("Order place failed");
      return   inventoryClient.updateStock(request);

    }

}
