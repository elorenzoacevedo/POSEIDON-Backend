package com.poseidon.inventory.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poseidon.inventory.model.ElectronicDevice;
import com.poseidon.inventory.model.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, String> {
    List<Item> findByName(String name);
    List<Item> findByBrand(String brand);
    List<Item> findByCategory(String category);
    Optional<ElectronicDevice> findBySerialNumber(String serialNumber);
     Optional<Item> findByBarcode(String barcode);
    List<Item> findByPrice(BigDecimal price);
    List<Item> findByPriceGreaterThanEqual(BigDecimal price);
    List<Item> findByPriceLessThanEqual(BigDecimal price);
    List<Item> findByQuantity(int quantity);
    List<Item> findByQuantityGreaterThanEqual(int quantity);
    List<Item> findByQuantityLessThanEqual(int quantity);
}
