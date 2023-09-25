package com.poseidon.inventory.controller;

import com.poseidon.inventory.model.Item;
import com.poseidon.inventory.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping("/items/addItem")
    public ResponseEntity<String> postItem(@RequestBody Item item) {
        return itemService.saveItem(item);
    }

    @GetMapping("/items/{barcode}")
    public ResponseEntity<String> getItemByBarcode(@PathVariable String barcode) {
        return itemService.findItemById(barcode);
    }

    @DeleteMapping("items/{barcode}")
    public ResponseEntity<String> removeItem(@PathVariable String barcode) {
        return itemService.deleteItem(barcode);
    }
}
