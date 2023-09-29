package com.poseidon.inventory.service.validator;

import com.poseidon.inventory.model.Item;
import com.poseidon.inventory.service.result.DatabaseOperationResult;
import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Builder
public class ItemDataValidator {
    private final List<String> errors = new ArrayList<>();
    public DatabaseOperationResult verifyItemDataIntegrity(Item item) {
        DatabaseOperationResult result = DatabaseOperationResult.builder().status(HttpStatus.OK.value()).build();
        checkNullAttributes(item);
        checkNegativeQuantifiers(item);
        if (!errors.isEmpty()) {
            result.setStatus(HttpStatus.BAD_REQUEST.value());
            result.setMessage(Map.of("error", errors));
        }
        return result;
    }

    private void checkNullAttributes(Item item) {
        List<String> nullAttributes = new ArrayList<>();
        if (item.getBarcode() == null) {
            nullAttributes.add("barcode");
        }
        if (item.getName() == null) {
            nullAttributes.add("name");
        }
        if (item.getPrice() == null) {
            nullAttributes.add("price");
        }
        if (item.getCategory() == null) {
           nullAttributes.add("category");
        }

        if (!nullAttributes.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder("field { ");
            nullAttributes.forEach(attribute -> stringBuilder.append(attribute).append(" "));
            stringBuilder.append("} cannot be null.");
            errors.add(stringBuilder.toString());
        }
    }

    private void checkNegativeQuantifiers(Item item) {
        List<String> negativeQuantifiers = new ArrayList<>();
        if (item.getQuantity() < 0) {
            negativeQuantifiers.add("quantity");
        }
        if (item.getPrice() != null && item.getPrice().floatValue() < 0) {
            negativeQuantifiers.add("price");
        }

        if (!negativeQuantifiers.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder("field { ");
            negativeQuantifiers.forEach(attribute -> stringBuilder.append(attribute).append(" "));
            stringBuilder.append("} cannot be negative.");
            errors.add(stringBuilder.toString());
        }
    }

}
