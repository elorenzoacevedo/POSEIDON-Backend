package com.poseidon.inventory.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ElectronicDevice extends Item {
    @Column(name = "serial_number", length = 75)
    private String serialNumber;
}
