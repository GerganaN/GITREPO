package com.skrill.interns.fitnesse;

public class AddItemsToCart {

    private String id;
    private int quantity;
    private Framework framework;

    public AddItemsToCart() {
        framework = new Framework();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String result() {
        return framework.addItem(id, quantity);
    }

    public int itemsCount() {
        return framework.itemsCount();
    }

}
