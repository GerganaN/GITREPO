package com.skrill.interns.cart;

import com.google.gson.Gson;

public class JsonConverter {

    private Gson converter;

    public JsonConverter() {
        converter = new Gson();
    }

    public Cart convertJsonToCart(String jsonCart) {
        return converter.fromJson(jsonCart, Cart.class);
    }

    public String convertCartToJson(Cart cart) {
        return converter.toJson(cart);
    }

    public String convertShopToJson(Shop shop) {
        return converter.toJson(shop);
    }
}
