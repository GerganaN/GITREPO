/*
 * $ Gergana Nikolova, Veselin Aleksandrov, Atanas Kereziev, Stanislav Burov$
 *
 * Copyright <2014> Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */
package com.skrill.interns.shoppingcartapi;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cartItem")
@XmlAccessorType(XmlAccessType.NONE)
public class CartItem extends Item {
    @XmlElement(name = "price")
    private BigDecimal price;

    public CartItem() {

    }

    public CartItem(Item item, BigDecimal price) {
        super(item.getId(), item.getQuantity());
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
