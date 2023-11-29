package com.poseidon.inventory.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// import com.google.zxing.WriterException;
import com.poseidon.inventory.model.ElectronicDevice;
import com.poseidon.inventory.model.Item;
import com.poseidon.inventory.service.ItemService;

@RestController
@RequestMapping("/items")
@CrossOrigin(origins = "http://localhost:3000")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping
    public ResponseEntity<String> postItem(@RequestBody Item item) {
        return itemService.saveItem(item);
    }

    @PostMapping("/electronics")
    public ResponseEntity<String> postElectronic(@RequestBody ElectronicDevice device) {
        return itemService.saveItem(device);
    }

    @PostMapping("/{barcode}")
    public ResponseEntity<String> updateItem(@PathVariable String barcode, @RequestBody Item item) {
        return itemService.updateItem(barcode, item);
    }

    @PostMapping("/decrease-quantity/{barcode}")
    public ResponseEntity<String> decreaseQuantity(@RequestParam int value, @PathVariable String barcode) {
        return itemService.decreaseItemQuantity(value, barcode);
    }

    @PostMapping("/electronics/{barcode}")
    public ResponseEntity<String> updateElectronic(@PathVariable String barcode, @RequestBody ElectronicDevice device) {
        return itemService.updateItem(barcode, device);
    }

    @GetMapping
    public ResponseEntity<String> getAllItems() {
        return itemService.findAllItems();
    }

    @GetMapping("/{barcode}")
    public ResponseEntity<String> getItemByBarcode(@PathVariable String barcode) {
        return itemService.findItemById(barcode);
    }

    @GetMapping("/search-name")
    public ResponseEntity<String> searchItemsByName(@RequestParam String name) {
        return itemService.findItemsByName(name);
    }

    @GetMapping("/search-brand")
    public ResponseEntity<String> searchItemsByBrand(@RequestParam String brand) {
        return itemService.findItemsByBrand(brand);
    }

    @GetMapping("/search-category")
    public ResponseEntity<String> searchItemsByCategory(@RequestParam String category) {
        return itemService.findItemsByCategory(category);
    }

    @GetMapping("search-serial")
    public ResponseEntity<String> searchItemsBySerialNumber(@RequestParam String serial) {
        return itemService.findItemBySerialNumber(serial);
    }

    @GetMapping("/search-price")
    public ResponseEntity<String> searchItemsByPrice(@RequestParam BigDecimal price) {
        return itemService.findItemsByPrice(price);
    }

    @GetMapping("/search-price-gte")
    public ResponseEntity<String> searchItemsByPriceGreaterThanEquals(@RequestParam BigDecimal price) {
        return itemService.findItemsByPriceGreaterThanEquals(price);
    }

    @GetMapping("/search-price-lte")
    public ResponseEntity<String> searchItemsByPriceLessThanEquals(@RequestParam BigDecimal price) {
        return itemService.findItemsByPriceLessThanEquals(price);
    }

    @GetMapping("/search-quantity")
    public ResponseEntity<String> searchItemsByQuantity(@RequestParam int quantity) {
        return itemService.findItemsByQuantity(quantity);
    }

    @GetMapping("/search-quantity-gte")
    public ResponseEntity<String> searchItemsByQuantityGreaterThanEquals(@RequestParam int quantity) {
        return itemService.findItemsByQuantityGreaterThanEquals(quantity);
    }

    @GetMapping("/search-quantity-lte")
    public ResponseEntity<String> searchItemsByQuantityLessThanEquals(@RequestParam int quantity) {
        return itemService.findItemsByQuantityLessThanEquals(quantity);
    }

    @DeleteMapping("/{barcode}")
    public ResponseEntity<String> removeItem(@PathVariable String barcode) {
        return itemService.deleteItem(barcode);
    }

    // @GetMapping("/generate-barcode/{fileName}")
    // public ResponseEntity<String> generateBarcodeImage(@PathVariable String
    // fileName) {
    // try {
    // itemService.generateBarcodeImage(fileName);
    // return ResponseEntity.ok("Barcode image generated successfully");
    // } catch (WriterException e) {
    // e.printStackTrace();
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error
    // generating barcode image");
    // }
    // catch (IOException e) {
    // e.printStackTrace();
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error
    // generating barcode image");
    // }
    // }

}
