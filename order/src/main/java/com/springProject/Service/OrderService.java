package com.springProject.Service;

import com.springProject.Client.InventoryClient;
import com.springProject.Pojo.Order;
import com.springProject.Repository.OrderRepository;
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
    public String placeOrder(Order order) {

System.out.println("order name:" + order.getItemName());
System.out.println("order quantity:" + order.getQuantity());
        boolean isStockAvailable = inventoryClient.checkStock(order.getItemName(), order.getQuantity());

        if (!isStockAvailable) {
            return "Order failed! Not enough stock.";
        }

        // ✅ Step 2: Save Order in Database
        orderRepository.save(order);

        // ✅ Step 3: Update stock in Inventory Service
        Map<String, Object> request = new HashMap<>();
        request.put("itemName", order.getItemName());
        request.put("quantity", order.getQuantity());
      return   inventoryClient.updateStock(request);

    }

}
