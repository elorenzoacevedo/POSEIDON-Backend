package com.poseidon.inventory.service.result;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DatabaseOperationResult {
    private int status;
    private Object message;
}
