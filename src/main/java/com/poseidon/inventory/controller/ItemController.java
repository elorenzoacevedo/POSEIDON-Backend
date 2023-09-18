package com.poseidon.inventory.controller;

import com.poseidon.inventory.model.Item;
import com.poseidon.inventory.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping("/addItem")
    public ResponseEntity<?> postItem(@RequestBody Item item) {
        return itemService.saveItem(item);
    }
}
