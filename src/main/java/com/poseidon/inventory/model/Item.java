package com.poseidon.inventory.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance( strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "items")
public class Item {

    @Id
    @Column(name = "barcode", updatable = false)
    private String barcode;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "brand", length = 50)
    private String brand;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "quantity", nullable = false)
    private int quantity = 1;

    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;
}
