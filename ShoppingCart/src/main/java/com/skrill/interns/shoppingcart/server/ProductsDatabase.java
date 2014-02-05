/*
 * $$ Gergana Nikolova, Veselin Aleksandrov, Atanas Kereziev, Stanislav Burov$$
 *
 * Copyright <2014> Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */
package com.skrill.interns.shoppingcart.server;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ProductsDatabase {

    private static Map<String, BigDecimal> products;

    static {
        products = new HashMap<String, BigDecimal>();
        products.put("shoes", BigDecimal.valueOf(80.00));
        products.put("jeans", BigDecimal.valueOf(120.00));
        products.put("jacket", BigDecimal.valueOf(300.00));
        products.put("t-shirt", BigDecimal.valueOf(40.00));
        products.put("hat", BigDecimal.valueOf(60.00));
        products.put("watch", BigDecimal.valueOf(200.00));
    }

    public static BigDecimal takeItemPrice(String itemId) {
        return products.get(itemId);
    }

    public static Map<String, BigDecimal> getProducts() {
        return products;
    }

    public static boolean containsItem(String name) {
        if (products.containsKey(name)) {
            return true;
        }
        return false;
    }
}
