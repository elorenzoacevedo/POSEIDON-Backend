package com.poseidon.inventory.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "electronic_devices")
public class ElectronicDevice extends Item {
    @Column(name = "serial_number", nullable = false, length = 75)
    private String serial_number;
}
