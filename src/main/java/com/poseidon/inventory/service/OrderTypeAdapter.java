package com.poseidon.inventory.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.poseidon.inventory.model.Item;
import com.poseidon.inventory.model.Order;
import com.poseidon.inventory.model.Purchase;

public class OrderTypeAdapter extends TypeAdapter<Order> {
    @Override
    public void write(JsonWriter out, Order order) throws IOException {
        out.beginObject();
        out.name("orderNumber").value(order.getOrderNumber());
        out.name("paymentMethod").value(order.getPaymentMethod());
        out.name("total").value(order.getTotal().toString());
        out.name("date").value(order.getDate().toString());
        out.name("purchases");
        out.beginArray();
        for (Purchase purchase : order.getPurchases()) {
            out.beginObject();
            out.name("id").value(purchase.getId());
            out.name("quantity").value(purchase.getQuantity());
            out.name("itemBarcode").value(purchase.getItem().getBarcode());
            out.endObject();
        }
        out.endArray();
        out.endObject();
    }

    @Override
    public Order read(JsonReader in) throws IOException {
        Order order = new Order();
        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            switch (name) {
                case "orderNumber":
                    order.setOrderNumber(in.nextLong());
                    break;
                case "paymentMethod":
                    order.setPaymentMethod(in.nextString());
                    break;
                case "total":
                    order.setTotal(new BigDecimal(in.nextString()));
                    break;
                case "date":
                    try {
                        String dateStr = in.nextString();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        order.setDate(new java.sql.Date(sdf.parse(dateStr).getTime()));
                    } catch (ParseException e) {
                        throw new IOException(e);
                    }
                    break;
                case "purchases":
                    Set<Purchase> purchases = new HashSet<>();
                    in.beginArray();
                    while (in.hasNext()) {
                        Purchase purchase = new Purchase();
                        in.beginObject();
                        while (in.hasNext()) {
                            String purchaseName = in.nextName();
                            switch (purchaseName) {
                                case "id":
                                    purchase.setId(in.nextLong());
                                    break;
                                case "quantity":
                                    purchase.setQuantity(in.nextInt());
                                    break;
                                case "itemBarcode":
                                    Item item = new Item();
                                    item.setBarcode(in.nextString());
                                    purchase.setItem(item);
                                    break;
                            }
                        }
                        in.endObject();
                        purchases.add(purchase);
                    }
                    in.endArray();
                    order.setPurchases(purchases);
                    break;
            }
        }
        in.endObject();
        return order;
    }
}