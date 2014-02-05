package com.skrill.interns.cart;

import java.nio.charset.Charset;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/showcart")
public class Read extends CherryServlet {

    private static final long serialVersionUID = 3126355397241435674L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        Cookie cartCookie = findCartCookie(cookies);
        if (cartCookie == null) {
            String responseMessage = "Your cart is empty.";
            respondToClient(response, responseMessage, 200, "plain");
            return;
        }
        String decriptedJsonCart = decryptCookie(cartCookie);
        if (decriptedJsonCart == null) {
            String responseMessage = "400 Bad Request\n\nCorrupted cookie!";
            respondToClient(response, responseMessage, 400, "plain");
            return;
        }
        String jsonCart = new String(decriptedJsonCart.getBytes(), Charset.forName("UTF-8"));
        Cart cart = jsonConverter.convertJsonToCart(jsonCart);
        showCart(cart, response);
    }
}
