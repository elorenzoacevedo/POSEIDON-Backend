package com.poseidon.inventory.repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poseidon.inventory.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByDate(Date date);
    List<Order> findByPaymentMethod(String string);
    List<Order> findByTotal(BigDecimal total);

}

