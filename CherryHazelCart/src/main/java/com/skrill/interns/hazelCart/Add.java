package com.skrill.interns.hazelCart;

import java.nio.charset.Charset;
import java.security.SecureRandom;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/add")
public class Add extends CherryServlet {

    private static final long serialVersionUID = -1476972183140572570L;

    private static SecureRandom random;

    static {
        random = new SecureRandom();
    }

    private void bakeCookie(String sessionId, HttpServletResponse response) {
        String cookieValue = new String(sessionId.getBytes(Charset.forName("UTF-8")), Charset.forName("UTF-8"));
        Cookie idCookie = new Cookie("ID", sessionId);
        idCookie.setMaxAge(20 * 60);
        response.addCookie(idCookie);
    }

    private Cart makeNewCart(int id, int quantity) {
        Cart newCart = new Cart();
        Item newItem = new Item(shop.getItems().get(id));
        newItem.setQuantity(quantity);
        newCart.addItem(id, newItem);
        return newCart;
    }

    private String makeNewSession(Cart cart) {
        String sessionId = String.valueOf(Thread.currentThread().getId() + System.currentTimeMillis());
        String cartJson = jsonConverter.convertCartToJson(cart);
        sessions.put(sessionId, cartJson);
        return sessionId;
    }

    private void updateCart(Cart cart, int id, int quantity) {
        Item oldItem = cart.getItems().get(id);
        if (oldItem == null) {
            Item newItem = new Item(shop.getItems().get(id));
            newItem.setQuantity(quantity);
            cart.addItem(id, newItem);
        } else {
            oldItem.setQuantity(oldItem.getQuantity() + quantity);
            cart.addItem(id, oldItem);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        Integer id = parseParameter(request.getParameter("id"));
        Integer quantity = parseParameter(request.getParameter("quantity"));

        String parametersStatus = getParametersStatus(id, quantity);
        if (!"OK".equals(parametersStatus)) {
            respondToClient(response, parametersStatus, 400, "plain");
            return;
        }

        Cookie[] cookies = request.getCookies();
        Cookie cartCookie = findCartCookie(cookies);
        Cart cart;
        if (cartCookie == null) {
            cart = makeNewCart(id, quantity);
            String sessionId = makeNewSession(cart);
            bakeCookie(sessionId, response);
            showCart(cart, response);
        } else {
            String sessionId = new String(cartCookie.getValue().getBytes(Charset.forName("UTF-8")), Charset.forName("UTF-8"));
            if (!sessions.containsKey(sessionId)) {
                String responseMessage = "400 Bad Request\n\nCorrupted cookie!";
                respondToClient(response, responseMessage, 400, "plain");
                return;
            }
            cart = getClientCart(sessionId);
            updateCart(cart, id, quantity);
            updateClientSession(sessionId, cart);
        }
        showCart(cart, response);
    }
}
