package com.poseidon.inventory.service;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.poseidon.inventory.model.Order;
import com.poseidon.inventory.model.Purchase;
import com.poseidon.inventory.repository.OrderRepository;
import com.poseidon.inventory.repository.PurchaseRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class OrderService {
    private final String NOT_FOUND = "Order not found.";
    private final String DELETED = "Order deleted successfully";
    private final String SAVED = "Order saved successfully";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    // Create
    public ResponseEntity<Order> saveOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        Long orderNumber = savedOrder.getOrderNumber();
        for (Purchase purchase : order.getPurchases()) {
            Order tempOrder = new Order();
            tempOrder.setOrderNumber(orderNumber);
            purchase.setOrder(tempOrder);
            purchaseRepository.save(purchase);
        }

        return ResponseEntity.ok(orderRepository.save(savedOrder));
    }

    // Delete
    public ResponseEntity<String> deleteOrder(Long orderNumber) {
        log.info("Deleting order with order number {}...", orderNumber);
        HttpStatus status;
        String result;
        if (!orderRepository.existsById(orderNumber)) {
            log.info(NOT_FOUND, orderNumber);
            status = HttpStatus.NOT_FOUND;
            result = NOT_FOUND;
        } else {
            orderRepository.deleteById(orderNumber);
            log.info(DELETED);
            status = HttpStatus.OK;
            result = DELETED;
        }

        return ResponseEntity.status(status).body(result);
    }
    // Warning. This operation will delete all the orders in the entity.
    public ResponseEntity<String> deleteAllOrders() {
        log.info("Deleting all orders...");
        HttpStatus status;
        String result;
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            log.info(NOT_FOUND);
            status = HttpStatus.NOT_FOUND;
            result = NOT_FOUND;
        } else {
            orderRepository.deleteAll();
            log.info(DELETED);
            status = HttpStatus.OK;
            result = "All orders have been deleted.";
        }

        return ResponseEntity.status(status).body(result);
    }

    // Update
    public ResponseEntity<Order> updateOrder(Long orderNumber, Order updatedOrder) {
        Order savedOrder = orderRepository.save(updatedOrder);
        return ResponseEntity.ok(savedOrder);
    }

    // Read
    public ResponseEntity<List<Order>> findAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return ResponseEntity.ok(orders);
    }

    public ResponseEntity<Optional<Order>> findOrderByOrdNum(Long orderNumber) {
        Optional<Order> order = orderRepository.findById(orderNumber);

        if (order.isPresent()) {
            // order.get().getItems().size();
        }

        return ResponseEntity.ok(order);
    }

    public ResponseEntity<List<Order>> findOrderByDate(Date date) {
        List<Order> orders = orderRepository.findByDate(date);
        return ResponseEntity.ok(orders);
    }

    public ResponseEntity<List<Order>> findOrderByPaymentMethod(String paymentMethod) {
        List<Order> orders = orderRepository.findByPaymentMethod(paymentMethod);
        return ResponseEntity.ok(orders);
    }

    public ResponseEntity<List<Order>> findOrderByTotal(BigDecimal total) {
        List<Order> orders = orderRepository.findByTotal(total);
        return ResponseEntity.ok(orders);
    }

    // Invoice Generator
    public ResponseEntity<String> generateInvoicePdf(Long orderNumber) {
        Optional<Order> orderOpt = orderRepository.findById(orderNumber);
        if (!orderOpt.isPresent()) {
            log.info(NOT_FOUND, orderNumber);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(NOT_FOUND);
        }

        Order order = orderOpt.get();

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(
                    "C:\\Users\\Justin Diaz Villa\\Documents\\Invoices\\Invoice_" + orderNumber + ".pdf"));
            document.open();

            // Create a new font object
            Font font = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);

            // Create a new paragraph with the title and the font
            Paragraph title = new Paragraph("POSEIDON INVOICE", font);

            // Set the alignment of the title to center
            title.setAlignment(Element.ALIGN_CENTER);

            // Add the title to the document
            document.add(title);

            // Create a table with 4 columns
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Add column headers
            Stream.of("Item and Brand", "Quantity", "Price", "Amount")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(columnTitle));
                        table.addCell(header);
                    });

            // Add rows
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (Purchase purchase : order.getPurchases()) {
                BigDecimal amount = purchase.getItem().getPrice().multiply(new BigDecimal(purchase.getQuantity()));
                totalAmount = totalAmount.add(amount);

                PdfPCell cell;

                cell = new PdfPCell(new Phrase(purchase.getItem().getName() + ", " + purchase.getItem().getBrand()));
                cell.setFixedHeight(30);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(purchase.getQuantity())));
                cell.setFixedHeight(30);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("$" + purchase.getItem().getPrice().toString()));
                cell.setFixedHeight(30);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("$" + amount.toString()));
                cell.setFixedHeight(30);
                table.addCell(cell);
            }
            // Add total amount
            PdfPCell cell = new PdfPCell(new Phrase("Total:"));
            cell.setColspan(3);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setFixedHeight(30);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("$" + totalAmount.toString()));
            cell.setFixedHeight(30);
            table.addCell(cell);

            // Add table to document
            document.add(table);

            // Add order date
            Paragraph orderDate = new Paragraph("Order Date: " + order.getDate());
            orderDate.setAlignment(Element.ALIGN_RIGHT);
            document.add(orderDate);
            document.close();
            return ResponseEntity.ok("Invoice generated successfully");
        } catch (Exception e) {
            log.error("Error generating invoice PDF", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating invoice");
        }
    }
}