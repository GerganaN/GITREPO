/*
 * $ Kaloyan Milkov, Atanas Kereziev, Stanislav Burov$
 *
 * Copyright <2013> Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */
package com.skrill.interns.appleWebCartHazel;

import static org.testng.Assert.assertEquals;

import java.math.BigDecimal;

import org.testng.annotations.Test;

public class CartTest {
    @Test
    public void test_when_addItem_is_given_item_which_is_not_present_in_cart_then_quantity_equalst_to_given_quantity()
            throws Exception {
        // GIVEN
        Cart cart = new Cart();
        String name = "shoes";
        int quantity = 10;
        BigDecimal price = BigDecimal.valueOf(5.17);
        // WHEN
        cart.addItem(name, quantity, price);
        int newQuantity = cart.getItem(name).getQuantity();
        // THEN
        assertEquals(newQuantity, quantity);
    }

    @Test
    public void test_when_addItem_is_given_item_which_is_present_in_cart_and_quantity_10and_new_quantity_10_then_quantity_20()
            throws Exception {
        // GIVEN
        Cart cart = new Cart();
        String name = "shoes";
        int quantity = 10;
        BigDecimal price = BigDecimal.valueOf(5.17);

        // WHEN
        cart.addItem(name, quantity, price);
        cart.addItem(name, quantity, price);
        int newQuantity = cart.getItem(name).getQuantity();
        // THEN
        assertEquals(newQuantity, 20);
    }

    @Test
    public void test_when_UpdateItem_quantity_given_10_then_new_quantity_10() throws Exception {
        // GIVEN
        Cart cart = new Cart();
        String name = "shoes";
        int quantity = 10;
        BigDecimal price = BigDecimal.valueOf(5.17);
        cart.addItem(name, quantity, price);
        // WHEN
        cart.updateItem(name, quantity);

        int newQuantity = cart.getItem(name).getQuantity();
        // THEN
        assertEquals(newQuantity, quantity);
    }

    @Test
    public void test_when_DeleteItem_shoes_then_cart_getItem_returns_null() throws Exception {
        // GIVEN
        Cart cart = new Cart();
        String name = "shoes";
        int quantity = 10;
        BigDecimal price = BigDecimal.valueOf(5.17);
        // WHEN
        cart.addItem(name, quantity, price);
        cart.deleteItem(name);

        // THEN
        assertEquals(cart.getItem(name), null);
    }

}
