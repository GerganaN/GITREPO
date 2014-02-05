package com.skrill.interns.hazelCart;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import com.hazelcast.client.ClientConfig;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;

public class CherryServlet extends HttpServlet {

    private static final long serialVersionUID = 1148755619116197976L;

    protected static Map<String, String> sessions;
    protected static Shop shop;
    protected static JsonConverter jsonConverter;

    static {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.getGroupConfig().setName("session_cache").setPassword("mbsession");
        clientConfig.addAddress("10.32.18.39", "10.32.18.55", "10.32.18.71", "10.32.18.87", "10.32.18.103", "10.32.18.40", "10.32.18.56",
                "10.32.18.72", "10.32.18.88", "10.32.18.104");
        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);

        sessions = client.getMap("cherry_sessions");
        shop = new Shop();
        jsonConverter = new JsonConverter();
    }

    protected Integer parseParameter(String parameter) {
        Integer result;
        try {
            result = Integer.parseInt(parameter);
        } catch (NumberFormatException e) {
            return null;
        }
        return result;
    }

    protected String getParametersStatus(Integer id, Integer quantity) {
        if (id == null) {
            return "400 Bad Request\n\nIncorrectly set id parameter";
        }
        if (quantity == null) {
            return "400 Bad Request\n\nIncorrectly set qunatity parameter";
        }
        if (!shop.getItems().containsKey(id)) {
            return "Sorry, we don't have item with this id. Please choose something else.";
        }
        if (quantity < 1) {
            return "400 Bad Request\n\nQuantity cannot be less than 1";
        }
        return "OK";
    }

    protected void showCart(Cart cart, HttpServletResponse response) {
        if (cart.getItems().isEmpty()) {
            String responseMessage = "Your cart is empty.";
            respondToClient(response, responseMessage, 200, "plain");
        } else {
            String responseMessage = jsonConverter.convertCartToJson(cart);
            respondToClient(response, responseMessage, 200, "json");
        }
    }

    protected Cookie findCartCookie(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if ("ID".equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }

    protected Cart getClientCart(String sessionId) {
        String cartJson = sessions.get(sessionId);
        Cart cart = jsonConverter.convertJsonToCart(cartJson);
        return cart;
    }

    protected void updateClientSession(String sessionId, Cart cart) {
        String cartJson = jsonConverter.convertCartToJson(cart);
        sessions.put(sessionId, cartJson);
    }

    protected void respondToClient(HttpServletResponse response, String responseMessage, int status, String type) {
        try {
            response.setStatus(status);
            response.setCharacterEncoding("UTF-8");
            response.setContentLength(responseMessage.getBytes(Charset.forName("UTF-8")).length);
            response.setContentType("text/" + type);
            response.setHeader("Connection", "close");
            response.getOutputStream().write(responseMessage.getBytes(Charset.forName("UTF-8")));
            response.flushBuffer();
        } catch (IOException e) {
            System.err.println("Could not respond to client");
            e.printStackTrace();
        }
    }
}
