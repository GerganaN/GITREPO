package com.skrill.interns.hazelCart;

import java.nio.charset.Charset;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/showcart")
public class Read extends CherryServlet {

    private static final long serialVersionUID = -633320581537642668L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        Cookie cartCookie = findCartCookie(cookies);
        if (cartCookie == null) {
            String responseMessage = "Your cart is empty.";
            respondToClient(response, responseMessage, 200, "plain");
            return;
        }
        String sessionId = new String(cartCookie.getValue().getBytes(Charset.forName("UTF-8")), Charset.forName("UTF-8"));
        if (!sessions.containsKey(sessionId)) {
            String responseMessage = "400 Bad Request\n\nCorrupted cookie!";
            respondToClient(response, responseMessage, 400, "plain");
            return;
        }
        Cart cart = getClientCart(sessionId);
        showCart(cart, response);
    }
}
