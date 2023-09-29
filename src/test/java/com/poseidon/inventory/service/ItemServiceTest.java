package com.poseidon.inventory.service;

import com.google.gson.Gson;
import com.poseidon.inventory.model.Item;
import com.poseidon.inventory.repository.ItemRepository;
import com.poseidon.inventory.service.result.DatabaseOperationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ItemServiceTest {
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;
    private Gson gson;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        gson = new Gson();
    }

    @Test
    public void saveItem_shouldReturnSuccessStatusCode_whenInputItemIsValid() {
        Item item = new Item("12345", "testItem", "someBrand",
                "testCategory", 2, new BigDecimal(4));
        when(itemRepository.save(item)).thenReturn(item);
        ResponseEntity<String> response = itemService.saveItem(item);

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void saveItem_shouldReturnBadRequestStatusCode_whenInputItemIsNotValid() {
        Item item = new Item("12345", null, "someBrand",
                "testCategory", 2, new BigDecimal(4));
        when(itemRepository.save(item)).thenReturn(item);
        ResponseEntity<String> response = itemService.saveItem(item);

        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    public void deleteItem_shouldReturnNotFoundStatusCode_whenItemIsNotFound() {
        String barcode = "testBarcode";
        when(itemRepository.existsById(barcode)).thenReturn(false);
        ResponseEntity<String> response = itemService.deleteItem(barcode);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    public void deleteItem_shouldDeleteItem_whenItemIsFound() {
        String barcode = "testBarcode";
        when(itemRepository.existsById(barcode)).thenReturn(true);
        ResponseEntity<String> response = itemService.deleteItem(barcode);

        verify(itemRepository).deleteById(barcode);
    }

    @Test
    public void deleteItem_shouldReturnOkStatusCode_whenItemIsFound() {
        String barcode = "testBarcode";
        when(itemRepository.existsById(barcode)).thenReturn(true);
        ResponseEntity<String> response = itemService.deleteItem(barcode);

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void updateItem_shouldReturnNotFoundStatusCode_whenItemIsNotFound() {
        String barcode = "testBarcode";
        when(itemRepository.existsById(barcode)).thenReturn(false);
        ResponseEntity<String> response = itemService.updateItem(barcode, any(Item.class));

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    public void updateItem_shouldReturnConflictStatusCode_whenBarcodeIsModified() {
        String barcode = "testBarcode";
        Item mockItem = mock(Item.class);
        String mockBarcode = "mockBarcode";
        when(itemRepository.existsById(barcode)).thenReturn(true);
        when(mockItem.getBarcode()).thenReturn(mockBarcode);
        ResponseEntity<String> response = itemService.updateItem(barcode, mockItem);

        assertEquals(409, response.getStatusCode().value());
    }

    @Test
    public void updateItem_shouldSaveItem_whenItemAndBarcodeAreValid() {
        String barcode = "testBarcode";
        Item item = new Item("testBarcode", "testItem", "someBrand",
                "testCategory", 2, new BigDecimal(4));
        when(itemRepository.existsById(barcode)).thenReturn(true);
        itemService.updateItem(barcode, item);

        verify(itemRepository).save(item);
    }

    @Test
    public void findItemById_shouldReturnNotFoundMessage_whenItemIsNotFound() {
        String barcode = "testBarcode";
        when(itemRepository.findById(barcode)).thenReturn(Optional.empty());
        ResponseEntity<String> response = itemService.findItemById(barcode);
        DatabaseOperationResult result = gson.fromJson(response.getBody(), DatabaseOperationResult.class);

        assertEquals(TestData.NOT_FOUND, result.getMessage());
    }

    @Test
    public void findItemById_shouldReturnItem_ifItemIsFound() {
        Item item = new Item("12345", null, "someBrand",
                "testCategory", 2, new BigDecimal(4));
        String barcode = "12345";
        when(itemRepository.findById(barcode)).thenReturn(Optional.of(item));
        ResponseEntity<String> response = itemService.findItemById(barcode);
        DatabaseOperationResult result = gson.fromJson(response.getBody(), DatabaseOperationResult.class);
        Item resultItem = gson.fromJson(gson.toJson(result.getMessage()), Item.class);
        resultItem.setPrice(new BigDecimal(4));

        assertEquals(item, resultItem);
    }
}
