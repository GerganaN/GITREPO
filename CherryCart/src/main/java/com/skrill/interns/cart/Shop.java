package com.skrill.interns.cart;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "shop")
@XmlAccessorType(XmlAccessType.FIELD)
public class Shop {

    private Map<Integer, Item> database;

    public Shop() {
        database = new HashMap<Integer, Item>();
        database.put(1, new Item(-1, "Тениска", BigDecimal.valueOf(15)));
        database.put(2, new Item(-1, "Дънки", BigDecimal.valueOf(20)));
        database.put(3, new Item(-1, "Блуза", BigDecimal.valueOf(30)));
        database.put(4, new Item(-1, "Панталон", BigDecimal.valueOf(40)));
        database.put(5, new Item(-1, "Компютър", BigDecimal.valueOf(500)));
        database.put(6, new Item(-1, "Стол", BigDecimal.valueOf(60)));
        database.put(7, new Item(-1, "Маса", BigDecimal.valueOf(150)));
        database.put(8, new Item(-1, "Портокал", BigDecimal.valueOf(1.5)));
        database.put(9, new Item(-1, "Баничка", BigDecimal.valueOf(0.9)));
        database.put(10, new Item(-1, "Яке", BigDecimal.valueOf(80)));
    }

    public Map<Integer, Item> getItems() {
        return database;
    }
}
