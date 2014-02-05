package com.skrill.interns.appleWebCart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Cart {
    private String id;
    private List<Item> itemsInCart;

    public Cart() {
        id = UUID.randomUUID().toString();
        itemsInCart = new ArrayList<Item>();
    }

    public String getId() {
        return id;
    }

    public Item getItem(String itemName) {
        for (Item item : itemsInCart) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }

    public void addItem(String name, int quantity, BigDecimal price) {
        Item item = getItem(name);

        if (item != null) {
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            item = new Item(name, quantity, price);
            itemsInCart.add(item);
        }
    }

    public void updateItem(String name, int quantity) {
        Item item = getItem(name);
        if (item != null) {
            item.setQuantity(quantity);
        }
    }

    public void deleteItem(String itemName) {
        Item item = getItem(itemName);
        if (item != null) {
            itemsInCart.remove(item);
        }
    }
}
