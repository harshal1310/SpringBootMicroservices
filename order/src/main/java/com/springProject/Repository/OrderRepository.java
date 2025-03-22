package com.springProject.Repository;

import com.springProject.OrderMicrosService;
import com.springProject.Pojo.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
