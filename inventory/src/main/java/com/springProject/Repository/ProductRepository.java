package com.springProject.Repository;

import com.springProject.Pojo.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByItemName(String itemName);
}
