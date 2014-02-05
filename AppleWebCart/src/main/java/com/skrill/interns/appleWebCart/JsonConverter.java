/*
 * $ Kaloyan Milkov, Atanas Kereziev, Stanislav Burov$
 *
 * Copyright <2013> Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */
package com.skrill.interns.appleWebCart;

import com.google.gson.Gson;

public class JsonConverter {
    private Gson gson;

    public JsonConverter() {
        gson = new Gson();
    }

    public String toJson(Cart cart) {
        return gson.toJson(cart);
    }

    public Cart toObject(String json) {
        return gson.fromJson(json, Cart.class);
    }

}
