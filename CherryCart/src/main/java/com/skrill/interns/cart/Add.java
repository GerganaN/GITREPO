package com.skrill.interns.cart;

import java.nio.charset.Charset;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/add")
public class Add extends CherryServlet {

    private static final long serialVersionUID = 6639083260253326482L;

    private Cart makeNewCart(int id, int quantity) {
        Cart newCart = new Cart();
        Item newItem = new Item(shop.getItems().get(id));
        newItem.setQuantity(quantity);
        newCart.addItem(id, newItem);
        return newCart;
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

        String idParameterStatus = validateId(id);
        String quantityParameterStatus = validateQuantity(quantity);

        if (!"OK".equals(idParameterStatus)) {
            respondToClient(response, idParameterStatus, 400, "plain");
            return;
        }
        if (!"OK".equalsIgnoreCase(quantityParameterStatus)) {
            respondToClient(response, quantityParameterStatus, 400, "plain");
            return;
        }

        Cookie[] cookies = request.getCookies();
        Cookie cartCookie = findCartCookie(cookies);
        Cart cart;
        if (cartCookie == null) {
            cart = makeNewCart(id, quantity);
            bakeCookie(cart, response);
        } else {
            String decriptedJsonCart = decryptCookie(cartCookie);
            if (decriptedJsonCart == null) {
                String responseMessage = "400 Bad Request\n\nCorrupted cookie!";
                respondToClient(response, responseMessage, 400, "plain");
                return;
            }
            String jsonCart = new String(decriptedJsonCart.getBytes(), Charset.forName("UTF-8"));
            cart = jsonConverter.convertJsonToCart(jsonCart);
            updateCart(cart, id, quantity);
            bakeCookie(cart, response);
        }
        showCart(cart, response);
    }
}
