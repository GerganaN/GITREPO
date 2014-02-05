/*
 * $ Gergana Nikolova, Veselin Aleksandrov, Atanas Kereziev, Stanislav Burov$
 *
 * Copyright <2014> Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */
package com.skrill.interns.fitnesse;

public class ShoppingScript {
    private Framework framework;

    public ShoppingScript() {
        framework = new Framework();
    }

    public boolean createCart() {
        return framework.createCart();
    }

    public void addItemWithIdAndQuantity(String id, int quantity) {
        framework.addItem(id, quantity);
    }

    public String updateItemWithIdAndQuantity(String id, int quantity) {
        return framework.updateItem(id, quantity);
    }

    public int getItemQuantity(String id) {
        return framework.getItemQuantity(id);
    }

    public int itemsCount() {
        return framework.itemsCount();
    }

    public String deleteItemWithId(String id) {
        return framework.deleteItem(id);
    }

}
