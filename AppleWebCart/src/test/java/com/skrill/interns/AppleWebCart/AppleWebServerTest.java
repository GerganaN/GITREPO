/*
 * $ Kaloyan Milkov, Atanas Kereziev, Stanislav Burov$
 *
 * Copyright <2013> Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */
package com.skrill.interns.AppleWebCart;

import java.math.BigDecimal;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.skrill.interns.appleWebCart.AppleWebServer;

public class AppleWebServerTest {
    AppleWebServer server;

    @BeforeTest
    public void setup() {
        server = new AppleWebServer();
    }

    @Test
    public void convertQuantity_when_quantity_is_null_quantity_is_one() throws Exception {
        // GIVEN
        String quantity = null;
        // WHEN
        int number = server.convertQuantity(quantity);
        // THEN
        Assert.assertTrue(number == -1);
    }

    @Test
    public void convertQuantity_when_quantity_is_word_quantity_is_one() throws Exception {
        // GIVEN
        String quantity = "quantity";
        // WHEN
        int number = server.convertQuantity(quantity);
        // THEN
        Assert.assertTrue(number == -1);
    }

    @Test
    public void convertQuantity_when_quantity_is_number_quantity_is_the_number() throws Exception {
        // GIVEN
        String quantity = "5";
        // WHEN
        int number = server.convertQuantity(quantity);
        // THEN
        Assert.assertTrue(number == 5);
    }

    @Test
    public void findUserCookie_when_user_dont_have_cookie_cookie_is_null() throws Exception {
        // GIVEN
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        // WHEN
        Cookie cookie = server.findCartCookie(request);
        // THEN
        Assert.assertNull(cookie);
    }

    @Test
    public void verifyUserCookie_when_price_is_null_return_null() throws Exception {
        // GIVEN
        String method = "add";
        int quantity = 5;
        BigDecimal price = null;
        // WHEN
        boolean check = server.verifyRequestParameters(method, quantity, price);
        // THEN
        Assert.assertEquals(check, false);
    }

    @Test
    public void verifyUserCookie_when_price_is_not_null_quantity_bigger_than_null_method_is_good() throws Exception {
        // GIVEN
        String method = "add";
        int quantity = 5;
        BigDecimal price = BigDecimal.valueOf(6.56);
        // WHEN
        boolean check = server.verifyRequestParameters(method, quantity, price);
        // THEN
        Assert.assertEquals(check, true);
    }

    @Test
    public void verifyUserCookie_when_price_is_not_null_quantity_bigger_than_null_method_is_not_good() throws Exception {
        // GIVEN
        String method = "testmethod";
        int quantity = 5;
        BigDecimal price = BigDecimal.valueOf(6.56);
        // WHEN
        boolean check = server.verifyRequestParameters(method, quantity, price);
        // THEN
        Assert.assertEquals(check, false);
    }

    @Test
    public void verifyUserCookie_when_price_is_not_null_quantity_not_bigger_than_null() throws Exception {
        // GIVEN
        String method = "add";
        int quantity = 0;
        BigDecimal price = BigDecimal.valueOf(6.56);
        // WHEN
        boolean check = server.verifyRequestParameters(method, quantity, price);
        // THEN
        Assert.assertEquals(check, false);
    }
}
