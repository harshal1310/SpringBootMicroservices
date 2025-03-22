package com.springProject.Pojo;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;
    private double price;
    private int quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product() {
    }

    public Product(Long id, String name, double price, int stock) {
        this.id = id;
        this.itemName = name;
        this.price = price;
        this.quantity = stock;
    }

    public String getItemName() {
        return itemName;
    }

    public void setName(String name) {
        this.itemName = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
