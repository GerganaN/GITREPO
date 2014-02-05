/*
 * $$ Gergana Nikolova, Veselin Aleksandrov, Atanas Kereziev, Stanislav Burov$$
 *
 * Copyright <2014> Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */
package com.skrill.interns.shoppingcartapi;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cart")
@XmlAccessorType(XmlAccessType.NONE)
public class Cart {

    @XmlElement
    private List<CartItem> items;
    @XmlElement
    private String cartId;

    public Cart() {
        items = new ArrayList<CartItem>();
    }

    public List<CartItem> getItems() {
        return items;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public CartItem getItem(String itemId) {
        for (CartItem cartItem : items) {
            if (cartItem.getId().equalsIgnoreCase(itemId)) {
                return cartItem;
            }
        }
        return null;
    }

    public void addItem(CartItem cartItem) {
        CartItem oldItem = getItem(cartItem.getId());
        if (oldItem != null) {
            oldItem.setQuantity(oldItem.getQuantity() + cartItem.getQuantity());
        } else {
            items.add(cartItem);
        }
    }

    public boolean updateItem(CartItem cartItem) {
        CartItem temporaryItem = getItem(cartItem.getId());
        if (temporaryItem != null) {
            temporaryItem.setQuantity(cartItem.getQuantity());
            return true;
        }
        return false;
    }

    public boolean deleteItem(Item item) {
        Item temporaryItem = getItem(item.getId());
        if (temporaryItem != null) {
            items.remove(temporaryItem);
            return true;
        }
        return false;
    }
}
