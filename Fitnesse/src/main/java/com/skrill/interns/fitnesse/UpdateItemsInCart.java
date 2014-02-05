package com.skrill.interns.fitnesse;

public class UpdateItemsInCart {
    private String updateItemID;
    private int updateItemQuantity;
    private String itemIdInCart;
    private int itemQuantityInCart;
    private Framework framework;

    public UpdateItemsInCart() {
        framework = new Framework();
    }

    public String getItemIdInCart() {
        return itemIdInCart;
    }

    public void setItemIdInCart(String itemIdInCart) {
        this.itemIdInCart = itemIdInCart;
    }

    public int getItemQuantityInCart() {
        return itemQuantityInCart;
    }

    public void setItemQuantityInCart(int itemQuantityInCart) {
        this.itemQuantityInCart = itemQuantityInCart;
    }

    public String getUpdateItemId() {
        return updateItemID;
    }

    public void setUpdateItemId(String id) {
        this.updateItemID = id;
    }

    public int getUpdateItemQuantity() {
        return updateItemQuantity;
    }

    public void setUpdateItemQuantity(int quantity) {
        this.updateItemQuantity = quantity;
    }

    public String update() {
        return framework.updateItem(updateItemID, updateItemQuantity);
    }

    public String id() {
        return framework.getSessionId();
    }

    public void execute() {
        framework.addItem(itemIdInCart, itemQuantityInCart);
    }

    public void reset() {
        framework.createCart();
        System.out.println("reset");
    }
}
