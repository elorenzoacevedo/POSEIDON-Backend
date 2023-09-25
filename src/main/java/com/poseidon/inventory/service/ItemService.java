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

import java.util.Optional;

@Service
@Log4j2
public class ItemService {
    private final Gson gson = new Gson();
    private final String NOT_FOUND = "Item not found.";
    private final String DELETED = "Item deleted successfully";
    private final String SAVED = "Item saved successfully";

    @Autowired
    private ItemRepository itemRepository;

    //Create
    public ResponseEntity<String> saveItem(Item item) {
        ItemDataValidator validator = ItemDataValidator.builder().build();
        DatabaseOperationResult result = validator.verifyItemDataIntegrity(item);
        if (result.getStatus() != HttpStatus.OK.value()) {
            log.error("Error while saving item: {}", result.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gson.toJson(result));
        }
        itemRepository.save(item);
        log.info(SAVED);
        result.setMessage(SAVED);
        return ResponseEntity.of(Optional.of(gson.toJson(result)));
    }

    //Delete
    public ResponseEntity<String> deleteItem(String barcode) {
        DatabaseOperationResult result = DatabaseOperationResult.builder().build();
        HttpStatus status;
        if (!itemRepository.existsById(barcode)) {
            result.setMessage(NOT_FOUND);
            status = HttpStatus.NOT_FOUND;
        }
        else {
            itemRepository.deleteById(barcode);
            result.setMessage(DELETED);
            status = HttpStatus.OK;
        }
        result.setStatus(status.value());
        return ResponseEntity.status(status).body(gson.toJson(result));
    }

    //Read
    public ResponseEntity<String> findItemById(String barcode) {
        DatabaseOperationResult result = DatabaseOperationResult.builder().status(HttpStatus.OK.value()).build();
        log.info("Searching for item with barcode {}...", barcode);
        result.setMessage(itemRepository.findById(barcode).orElse(null));
        if (result.getMessage() == null) {
            result.setMessage(NOT_FOUND);
            log.info(NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(result));
    }
}
