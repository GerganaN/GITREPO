package com.skrill.interns.hazelCart;

import java.nio.charset.Charset;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/delete")
public class Delete extends CherryServlet {
    private static final long serialVersionUID = -2717427706243085121L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        Integer id = parseParameter(request.getParameter("id"));

        String parametersStatus = getParametersStatus(id, 1);
        if (!"OK".equals(parametersStatus)) {
            respondToClient(response, parametersStatus, 400, "plain");
            return;
        }

        Cookie[] cookies = request.getCookies();
        Cookie cartCookie = findCartCookie(cookies);
        if (cartCookie == null) {
            String responseMessage = "400 Bad Request\n\nCould not delete. Please add something to the cart first!";
            respondToClient(response, responseMessage, 400, "plain");
            return;
        }
        String sessionId = new String(cartCookie.getValue().getBytes(Charset.forName("UTF-8")), Charset.forName("UTF-8"));
        if (!sessions.containsKey(sessionId)) {
            String responseMessage = "400 Bad Request\n\nCorrupted cookie!";
            respondToClient(response, responseMessage, 400, "plain");
            return;
        }
        Cart cart = getClientCart(sessionId);
        if (!cart.getItems().containsKey(id)) {
            String responseMessage = "400 Bad Request\n\nSorry, you don't have item with this id. Please choose something else.";
            respondToClient(response, responseMessage, 400, "plain");
            return;
        }
        cart.removeItem(id);
        updateClientSession(sessionId, cart);
        showCart(cart, response);
    }
}
