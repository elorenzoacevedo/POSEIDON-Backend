package com.poseidon.inventory.service;

import com.google.gson.Gson;
import com.poseidon.inventory.model.Item;
import com.poseidon.inventory.repository.ItemRepository;
import com.poseidon.inventory.service.result.DatabaseOperationResult;
import com.poseidon.inventory.service.validator.ItemDataValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Log4j2
public class ItemService {
    private final Gson gson = new Gson();
    private final ItemDataValidator validator = ItemDataValidator.builder().build();

    @Autowired
    private ItemRepository itemRepository;

    public ResponseEntity<String> saveItem(Item item) {
        DatabaseOperationResult result = validator.verifyItemDataIntegrity(item);
        if (result.getStatus() != HttpStatus.OK.value()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gson.toJson(result));
        }
        itemRepository.save(item);
        result.setMessage("Item saved successfully.");
        return ResponseEntity.of(Optional.of(gson.toJson(result)));
    }
}
