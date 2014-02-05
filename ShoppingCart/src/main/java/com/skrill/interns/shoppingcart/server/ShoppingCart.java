/*
 * $$ Gergana Nikolova, Veselin Aleksandrov, Atanas Kereziev, Stanislav Burov$$
 *
 * Copyright <2014> Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */
package com.skrill.interns.shoppingcart.server;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.spi.NoLogWebApplicationException;

import com.skrill.interns.shoppingcartapi.Cart;
import com.skrill.interns.shoppingcartapi.CartItem;
import com.skrill.interns.shoppingcartapi.Item;
import com.skrill.interns.shoppingcartapi.ServiceError;
import com.skrill.interns.shoppingcartapi.ShoppingCartInterface;

public class ShoppingCart implements ShoppingCartInterface {
    @Context
    private HttpServletResponse response;

    @Override
    public Cart viewCart(String sessionId) {
        String sid = sessionId;
        Cart cart = Database.get(sid);
        if (cart == null) {
            sid = generateId();
            cart = new Cart();
            cart.setCartId(sid);
            Database.put(sid, cart);
            Cookie cookie = new Cookie("sessionId", sid);
            response.addCookie(cookie);
        }
        return cart;
    }

    @Override
    public Map<String, BigDecimal> showProducts() {
        return ProductsDatabase.getProducts();
    }

    @Override
    public Cart add(Item item, String sessionId) {
        String sid = sessionId;
        Cart cart = Database.get(sid);

        if (cart == null) {
            cart = new Cart();
            sid = generateId();
            cart.setCartId(sid);
            Cookie cookie = new Cookie("sessionId", sid);
            response.addCookie(cookie);
        }

        if (ProductsDatabase.containsItem(item.getId())) {
            if (item.getQuantity() > 0) {
                CartItem cartItem = new CartItem(item, ProductsDatabase.takeItemPrice(item.getId()));
                cart.addItem(cartItem);
            } else {
                throw new NoLogWebApplicationException(Response.status(400)
                        .entity(new ServiceError("Invalid quantity")).build());
            }
        } else {
            throw new NoLogWebApplicationException(Response.status(400).entity(new ServiceError("Invalid item"))
                    .build());
        }
        Database.put(sid, cart);
        return cart;
    }

    @Override
    public Cart update(Item item, String sessionId) {
        String sid = sessionId;
        Cart cart = Database.get(sid);

        if (cart != null) {
            if (ProductsDatabase.containsItem(item.getId())) {
                if (item.getQuantity() > 0) {
                    CartItem cartItem = new CartItem(item, ProductsDatabase.takeItemPrice(item.getId()));
                    if (!cart.updateItem(cartItem)) {
                        throw new NoLogWebApplicationException(Response.status(400)
                                .entity(new ServiceError("No such item in the cart")).build());
                    }
                    Database.put(sid, cart);
                } else {
                    throw new NoLogWebApplicationException(Response.status(400)
                            .entity(new ServiceError("Invalid quantity")).build());
                }
            } else {
                throw new NoLogWebApplicationException(Response.status(400).entity(new ServiceError("Invalid item"))
                        .build());
            }
        }
        return cart;
    }

    @Override
    public Cart delete(Item item, String sessionId) {
        String sid = sessionId;
        Cart cart = Database.get(sessionId);

        if (cart != null) {
            if (ProductsDatabase.containsItem(item.getId())) {
                CartItem cartItem = new CartItem(item, ProductsDatabase.takeItemPrice(item.getId()));
                if (!cart.deleteItem(cartItem)) {
                    throw new NoLogWebApplicationException(Response.status(400)
                            .entity(new ServiceError("No such item in the cart")).build());
                }
                Database.put(sid, cart);
            } else {
                throw new NoLogWebApplicationException(Response.status(400).entity(new ServiceError("Invalid item"))
                        .build());
            }
        }
        return cart;
    }

    private static String generateId() {
        String randomUUID = UUID.randomUUID().toString();
        String currentTime = Long.toString(System.currentTimeMillis());
        String threadId = Long.toString(Thread.currentThread().getId());
        return randomUUID + currentTime + threadId;
    }
}
