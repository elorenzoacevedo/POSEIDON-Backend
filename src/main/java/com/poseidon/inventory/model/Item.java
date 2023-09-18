package com.poseidon.inventory.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance( strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "items")
public class Item {

    @Id
    @Column(name = "barcode")
    private String barcode;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "brand", length = 50)
    private String brand;

    @Column(name = "quantity", nullable = false)
    private int quantity = 1;

}
