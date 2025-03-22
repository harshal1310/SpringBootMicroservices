package com.springProject.Controller;

import com.springProject.Pojo.Order;
import com.springProject.Service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService order;

    public OrderController(OrderService order) {
        this.order = order;
    }

   // @GetMapping
    //public List<Order> getAllOrders() {
      //  return order.getAllOrders();
    //}

    @PostMapping
    public String placeOrder(@RequestBody Order order) {
       return   this.order.placeOrder(order);
    }
}
