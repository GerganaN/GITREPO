package com.skrill.interns.cart;

import java.nio.charset.Charset;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/update")
public class Update extends CherryServlet {

    private static final long serialVersionUID = -4556201665035076096L;

    protected void updateCart(Cart cart, int id, int quantity) {
        Item oldItem = cart.getItems().get(id);
        oldItem.setQuantity(quantity);
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
        if (cartCookie == null) {
            String responseMessage = "400 Bad Request\n\nCould not update. Please add the item to the cart first!";
            respondToClient(response, responseMessage, 400, "plain");
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
        if (!cart.getItems().containsKey(id)) {
            String responseMessage = "400 Bad Request\n\nYou don't have item with this id. Please choose something else.";
            respondToClient(response, responseMessage, 400, "plain");
            return;
        }
        updateCart(cart, id, quantity);
        bakeCookie(cart, response);
        showCart(cart, response);
    }
}
