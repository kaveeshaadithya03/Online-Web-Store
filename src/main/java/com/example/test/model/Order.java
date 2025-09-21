package com.example.test.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Document(collection = "orders")
public class Order {
    @Id
    @NotBlank(message = "Order ID is required")
    private String orderID;

    @Positive(message = "Price must be positive")
    private double price;

    public Order() {}

    public Order(String orderID, double price) {
        this.orderID = orderID;
        this.price = price;
    }

    public String getOrderID() { return orderID; }
    public void setOrderID(String orderID) { this.orderID = orderID; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}