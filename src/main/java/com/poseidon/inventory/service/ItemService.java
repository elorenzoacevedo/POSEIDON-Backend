package com.poseidon.inventory.service;

import com.google.gson.Gson;
import com.poseidon.inventory.model.Item;
import com.poseidon.inventory.repository.ItemRepository;
import com.poseidon.inventory.service.result.DatabaseOperationResult;
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

    @Autowired
    private ItemRepository itemRepository;

    public ResponseEntity<String> saveItem(Item item) {
        Gson gson = new Gson();
        DatabaseOperationResult result = verifyItemDataIntegrity(item);
        if (result.getStatus() != HttpStatus.OK.value()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gson.toJson(result));
        }
        itemRepository.save(item);
        result.setMessage("Item saved successfully.");
        return ResponseEntity.of(Optional.of(gson.toJson(result)));
    }

    private DatabaseOperationResult verifyItemDataIntegrity(Item item) {
        DatabaseOperationResult result = DatabaseOperationResult.builder().status(HttpStatus.OK.value()).build();
        List<String> errors = new ArrayList<>();
        checkNullAttributes(item, errors);
        if (!errors.isEmpty()) {
            result.setStatus(HttpStatus.BAD_REQUEST.value());
            result.setMessage(Map.of("error", errors));
        }
        return result;
    }

    private void checkNullAttributes(Item item, List<String> errors) {
        List<String> nullAttributes = new ArrayList<>();
        if (item.getBarcode() == null) {
            nullAttributes.add("barcode");
        }
        if (item.getName() == null) {
            nullAttributes.add("name");
        }
        if (!nullAttributes.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder("field {");
            nullAttributes.forEach(attribute -> {
                stringBuilder.append(" ").append(attribute).append(" ");
            });
            stringBuilder.append("} cannot be null");
            errors.add(stringBuilder.toString());
        }
    }

}
