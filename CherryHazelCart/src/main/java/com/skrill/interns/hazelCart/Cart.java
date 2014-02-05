package com.skrill.interns.hazelCart;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cart")
@XmlAccessorType(XmlAccessType.FIELD)
public class Cart {

    Map<Integer, Item> items;

    public Cart() {
        items = new HashMap<Integer, Item>();
    }

    public Map<Integer, Item> getItems() {
        return items;
    }

    public void addItem(int id, Item item) {
        items.put(id, item);
    }

    public void removeItem(int id) {
        items.remove(id);
    }
}
