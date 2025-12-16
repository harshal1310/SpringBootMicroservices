package com.springProject.Repository;

import com.springProject.Pojo.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByItemNameIgnoreCase(String itemName);
}
