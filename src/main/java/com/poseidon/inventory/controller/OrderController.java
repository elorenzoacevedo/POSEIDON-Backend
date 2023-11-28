package com.poseidon.inventory.controller;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poseidon.inventory.model.Order;
import com.poseidon.inventory.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> postOrder(@RequestBody Order order) {

        return orderService.saveOrder(order);

    }

    @DeleteMapping("/{orderNumber}")
    public ResponseEntity<String> removeOrder(@PathVariable Long orderNumber) {

        return orderService.deleteOrder(orderNumber);
    }

    // Warning. This operation will delete all the orders in the entity.
    @DeleteMapping("/all")
    public ResponseEntity<String> removeAllOrders() {
        return orderService.deleteAllOrders();
    }

    @PostMapping("/{orderNumber}")
    public ResponseEntity<Order> updatedOrder(@PathVariable Long orderNumber, @RequestBody Order order) {
        return orderService.updateOrder(orderNumber, order);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {

        return orderService.findAllOrders();

    }

    @GetMapping("/{orderNumber}")
    public ResponseEntity<Optional<Order>> getOrderByOrdNum(@PathVariable Long orderNumber) {

        return orderService.findOrderByOrdNum(orderNumber);

    }

    @GetMapping("/search-date")
    public ResponseEntity<List<Order>> getOrderByDate(@RequestParam Date date) {
        java.sql.Date dateSQL = new java.sql.Date(date.getTime());
        return orderService.findOrderByDate(dateSQL);
    }

    @GetMapping("/search-payment-method")
    public ResponseEntity<List<Order>> getOrderByPaymentMethod(@RequestParam String paymentMethod) {
        return orderService.findOrderByPaymentMethod(paymentMethod);
    }

    @GetMapping("/search-total")
    public ResponseEntity<List<Order>> getOrderByTotal(@RequestParam BigDecimal total) {
        return orderService.findOrderByTotal(total);
    }

    @GetMapping("/{orderNumber}/invoice")
    public ResponseEntity<String> generateInvoice(@PathVariable Long orderNumber) {
        return orderService.generateInvoicePdf(orderNumber);
    }

}