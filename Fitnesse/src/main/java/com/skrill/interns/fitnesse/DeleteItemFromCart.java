/*
 * $ Gergana Nikolova, Veselin Aleksandrov, Atanas Kereziev, Stanislav Burov$
 *
 * Copyright <2013> Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */
package com.skrill.interns.fitnesse;

public class DeleteItemFromCart {
    private String deleteItemID;
    private String itemIdInCart;
    private int itemQuantityInCart;

    private Framework framework;

    public DeleteItemFromCart() {
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

    public String getDeleteItemId() {
        return deleteItemID;
    }

    public void setDeleteItemId(String id) {
        this.deleteItemID = id;
    }

    public String delete() {
        return framework.deleteItem(deleteItemID);
    }

    public void execute() {
        framework.addItem(itemIdInCart, itemQuantityInCart);
    }

    public void reset() {
        framework.createCart();
    }
}
