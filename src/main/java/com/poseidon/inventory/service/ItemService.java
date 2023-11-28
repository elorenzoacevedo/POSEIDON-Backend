package com.poseidon.inventory.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
// import com.google.zxing.BarcodeFormat;
// import com.google.zxing.WriterException;
// import com.google.zxing.client.j2se.MatrixToImageWriter;
// import com.google.zxing.common.BitMatrix;
// import com.google.zxing.oned.Code128Writer;
import com.poseidon.inventory.model.Item;
import com.poseidon.inventory.repository.ItemRepository;
import com.poseidon.inventory.service.result.DatabaseOperationResult;
import com.poseidon.inventory.service.validator.ItemDataValidator;

import lombok.extern.log4j.Log4j2;

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
        if (itemRepository.existsById(item.getBarcode())) {
            result.setMessage("Error: Item already exists.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gson.toJson(result));
        }

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
        log.info("Deleting item with barcode {}...", barcode);
        if (!itemRepository.existsById(barcode)) {
            result.setMessage(NOT_FOUND);
            log.info(NOT_FOUND);
            status = HttpStatus.NOT_FOUND;
        }
        else {
            itemRepository.deleteById(barcode);
            result.setMessage(DELETED);
            log.info(DELETED);
            status = HttpStatus.OK;
        }
        result.setStatus(status.value());
        return ResponseEntity.status(status).body(gson.toJson(result));
    }

    //Update
    public ResponseEntity<String> updateItem(String barcode, Item updatedItem) {
        DatabaseOperationResult result = DatabaseOperationResult.builder().build();
        log.info("Updating item with barcode {}...", barcode);
        if (!itemRepository.existsById(barcode)) {
            result.setMessage(NOT_FOUND);
            log.info(NOT_FOUND);
            result.setStatus(HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(gson.toJson(result));
        }

        if (!updatedItem.getBarcode().equals(barcode)) {
            result.setMessage("Cannot modify { barcode } field");
            log.error("Error: Cannot modify { barcode } field");
            result.setStatus(HttpStatus.CONFLICT.value());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(gson.toJson(result));
        }

        ItemDataValidator validator = ItemDataValidator.builder().build();
        result = validator.verifyItemDataIntegrity(updatedItem);
        if (result.getStatus() != HttpStatus.OK.value()) {
            log.error("Error while saving item: {}", result.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gson.toJson(result));
        }
        itemRepository.save(updatedItem);
        log.info(SAVED);
        result.setMessage(SAVED);
        return ResponseEntity.of(Optional.of(gson.toJson(result)));
    }

    //Read
    public ResponseEntity<String> findAllItems() {
        DatabaseOperationResult result = DatabaseOperationResult.builder().status(HttpStatus.OK.value()).build();
        result.setMessage(itemRepository.findAll());
        return ResponseEntity.of(Optional.of(gson.toJson(result)));
    }

    public ResponseEntity<String> findItemById(String barcode) {
        DatabaseOperationResult result = DatabaseOperationResult.builder().status(HttpStatus.OK.value()).build();
        HttpStatus status = HttpStatus.OK;
        log.info("Searching for item with barcode {}...", barcode);
        result.setMessage(itemRepository.findById(barcode).orElse(null));
        if (result.getMessage() == null) {
            result.setMessage(NOT_FOUND);
            status = HttpStatus.NOT_FOUND;
            log.info(NOT_FOUND);
        }
        result.setStatus(status.value());
        return ResponseEntity.status(status).body(gson.toJson(result));
    }

    public ResponseEntity<String> findItemsByName(String name) {
        log.info("Searching items with name: {}...", name);
        List<Item> items = itemRepository.findByName(name);
        DatabaseOperationResult result = DatabaseOperationResult.builder()
                .status(HttpStatus.OK.value())
                .message(items)
                .build();
        return ResponseEntity.of(Optional.of(gson.toJson(result)));
    }

    public ResponseEntity<String> findItemsByBrand(String brand) {
        log.info("Searching items with brand: {}", brand);
        List<Item> items = itemRepository.findByBrand(brand);
        DatabaseOperationResult result = DatabaseOperationResult.builder()
                .status(HttpStatus.OK.value())
                .message(items)
                .build();
        return ResponseEntity.of(Optional.of(gson.toJson(result)));
    }

    public ResponseEntity<String> findItemsByCategory(String category) {
        log.info("Searching items with category: {}", category);
        List<Item> items = itemRepository.findByCategory(category);
        DatabaseOperationResult result = DatabaseOperationResult.builder()
                .status(HttpStatus.OK.value())
                .message(items)
                .build();
        return ResponseEntity.of(Optional.of(gson.toJson(result)));
    }

    public ResponseEntity<String> findItemBySerialNumber(String serial) {
        DatabaseOperationResult result = DatabaseOperationResult.builder().build();
        HttpStatus status = HttpStatus.OK;
        log.info("Searching for item with serial {}...", serial);
        result.setMessage(itemRepository.findBySerialNumber(serial).orElse(null));
        if (result.getMessage() == null) {
            result.setMessage(NOT_FOUND);
            status = HttpStatus.NOT_FOUND;
            log.info(NOT_FOUND);
        }
        result.setStatus(status.value());
        return ResponseEntity.status(status).body(gson.toJson(result));
    }

    public ResponseEntity<String> findItemsByPrice(BigDecimal price) {
        log.info("Searching items with price: {}", price);
        List<Item> items = itemRepository.findByPrice(price);
        DatabaseOperationResult result = DatabaseOperationResult.builder()
                .status(HttpStatus.OK.value())
                .message(items)
                .build();
        return ResponseEntity.of(Optional.of(gson.toJson(result)));
    }

    public ResponseEntity<String> findItemsByPriceGreaterThanEquals(BigDecimal price) {
        log.info("Searching items with price gte: {}", price);
        List<Item> items = itemRepository.findByPriceGreaterThanEqual(price);
        DatabaseOperationResult result = DatabaseOperationResult.builder()
                .status(HttpStatus.OK.value())
                .message(items)
                .build();
        return ResponseEntity.of(Optional.of(gson.toJson(result)));
    }

    public ResponseEntity<String> findItemsByPriceLessThanEquals(BigDecimal price) {
        log.info("Searching items with price lte: {}", price);
        List<Item> items = itemRepository.findByPriceLessThanEqual(price);
        DatabaseOperationResult result = DatabaseOperationResult.builder()
                .status(HttpStatus.OK.value())
                .message(items)
                .build();
        return ResponseEntity.of(Optional.of(gson.toJson(result)));
    }

    public ResponseEntity<String> findItemsByQuantity(int quantity) {
        log.info("Searching items with quantity: {}", quantity);
        List<Item> items = itemRepository.findByQuantity(quantity);
        DatabaseOperationResult result = DatabaseOperationResult.builder()
                .status(HttpStatus.OK.value())
                .message(items)
                .build();
        return ResponseEntity.of(Optional.of(gson.toJson(result)));
    }

    public ResponseEntity<String> findItemsByQuantityGreaterThanEquals(int quantity) {
        log.info("Searching items with quantity gte: {}", quantity);
        List<Item> items = itemRepository.findByQuantityGreaterThanEqual(quantity);
        DatabaseOperationResult result = DatabaseOperationResult.builder()
                .status(HttpStatus.OK.value())
                .message(items)
                .build();
        return ResponseEntity.of(Optional.of(gson.toJson(result)));
    }

    public ResponseEntity<String> findItemsByQuantityLessThanEquals(int quantity) {
        log.info("Searching items with quantity lte: {}", quantity);
        List<Item> items = itemRepository.findByQuantityLessThanEqual(quantity);
        DatabaseOperationResult result = DatabaseOperationResult.builder()
                .status(HttpStatus.OK.value())
                .message(items)
                .build();
        return ResponseEntity.of(Optional.of(gson.toJson(result)));
    }

    //Barcode Generator

    // public void generateBarcodeImage(String fileName) throws WriterException, IOException {
    //     Random random = new Random();
    //     String barcodeText;
    //     do {
    //         barcodeText = String.format("%012d", random.nextInt(1_000_000_000));
    //     } while (itemRepository.existsById(barcodeText));
    
    //     Code128Writer barcodeWriter = new Code128Writer();
    
    //     BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.CODE_128, 1920, 1080);
    
    //     // Convert BitMatrix to BufferedImage
    //     BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
    
    //     // Create a new image with more space for text
    //     BufferedImage extendedImage = new BufferedImage(image.getWidth(), image.getHeight() + 60, BufferedImage.TYPE_INT_RGB);
    //     Graphics2D g = (Graphics2D) extendedImage.getGraphics();
    
    //     // Set the background color to white
    //     g.setColor(Color.WHITE);
    //     g.fillRect(0, 0, extendedImage.getWidth(), extendedImage.getHeight());
    
    //     // Draw the original image on the new image
    //     g.drawImage(image, 0, 0, null);
    
    //     // Add text below the barcode
    //     g.setColor(Color.BLACK);
    //     g.setFont(new Font("Arial", Font.PLAIN, 24)); 
    
    //     // Calculate the x position to center the text
    //     int x = (image.getWidth() - g.getFontMetrics().stringWidth(barcodeText)) / 2;
    
    //     g.drawString(barcodeText, x, image.getHeight() + 50); // Adjust text position
    
    //     // Save the new image
    //     String directory = "C:\\Users\\Justin Diaz Villa\\Documents\\Invoices\\";
    //     Path path = FileSystems.getDefault().getPath(directory + fileName + ".png");
    //     ImageIO.write(extendedImage, "PNG", path.toFile());
    // }
}
