package com.example.test.controller;

import com.example.test.model.Order;
import com.example.test.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@Validated
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @GetMapping("/{orderID}")
    public ResponseEntity<Order> getOrderById(@PathVariable String orderID) {
        Optional<Order> order = orderRepository.findById(orderID);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Order createOrder(@Valid @RequestBody Order order) {
        return orderRepository.save(order);
    }

    @PutMapping("/{orderID}")
    public ResponseEntity<Order> updateOrder(@PathVariable String orderID, @Valid @RequestBody Order updatedOrder) {
        Optional<Order> existingOrder = orderRepository.findById(orderID);
        if (!existingOrder.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        updatedOrder.setOrderID(orderID);
        Order savedOrder = orderRepository.save(updatedOrder);
        return ResponseEntity.ok(savedOrder);
    }

    @DeleteMapping("/{orderID}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String orderID) {
        if (orderRepository.existsById(orderID)) {
            orderRepository.deleteById(orderID);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}