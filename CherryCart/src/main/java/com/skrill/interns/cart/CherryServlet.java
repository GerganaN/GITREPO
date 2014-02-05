package com.skrill.interns.cart;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

public class CherryServlet extends HttpServlet {

    private static final long serialVersionUID = -4057767342896388449L;
    protected static Shop shop;
    private static Encryption crypt;
    protected static XmlEncoder encoder;
    protected static JsonConverter jsonConverter;

    static {
        shop = new Shop();
        crypt = new Encryption();
        encoder = new XmlEncoder();
        jsonConverter = new JsonConverter();
    }

    protected void bakeCookie(Cart cart, HttpServletResponse response) {
        String encryptedCookie = encryptCookie(jsonConverter.convertCartToJson(cart));
        Cookie cartCookie = new Cookie("CART", encryptedCookie);
        cartCookie.setMaxAge(20 * 60);
        response.addCookie(cartCookie);
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

    protected String validateId(Integer id) {
        if (id == null) {
            return "400 Bad Request\n\nIncorrectly set id parameter";
        }
        if (!shop.getItems().containsKey(id)) {
            return "400 Bad Request\n\nWe don't have item with this id. Please choose something else.";
        }

        return "OK";
    }

    protected String validateQuantity(Integer quantity) {
        if (quantity == null) {
            return "400 Bad Request\n\nIncorrectly set qunatity parameter";
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
            if ("CART".equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }

    protected String encryptCookie(String cookie) {
        byte[] cookieValue = crypt.encrypt(cookie);
        String encryptedCookie = new String(cookieValue);
        return encryptedCookie;
    }

    protected String decryptCookie(Cookie encryptedCookie) {
        byte[] encryptedCookieBytes = encryptedCookie.getValue().getBytes(Charset.forName("UTF-8"));
        String decryptedCookie = crypt.decrypt(encryptedCookieBytes);
        return decryptedCookie;
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
