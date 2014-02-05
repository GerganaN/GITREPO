package com.skrill.interns.appleWebCart;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ItemDatabase {
    private static Map<String, BigDecimal> itemsMap = new HashMap<String, BigDecimal>();

    static {
        itemsMap.put("shoes", BigDecimal.valueOf(80.00));
        itemsMap.put("jeans", BigDecimal.valueOf(120.00));
        itemsMap.put("jacket", BigDecimal.valueOf(300.00));
        itemsMap.put("t-shirt", BigDecimal.valueOf(40.00));
        itemsMap.put("hat", BigDecimal.valueOf(60.00));
        itemsMap.put("watch", BigDecimal.valueOf(200.00));
    }

    public static BigDecimal takeItemPrice(String itemName) {
        return itemsMap.get(itemName);
    }

    public static boolean containsItem(String name) {
        if (itemsMap.containsKey(name)) {
            return true;
        }
        return false;
    }

}
