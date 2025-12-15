package com.springProject.Pojo;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String itemName;
    @NotNull
    private double price;
    @NotNull
    private int quantity;

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    String msg;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product() {
    }

    public Product(Long id, String name, double price, int stock, String msg) {
        this.id = id;
        this.itemName = name;
        this.price = price;
        this.quantity = stock;
        this.msg = msg;
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
