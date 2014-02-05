package com.skrill.interns.appleWebCart;

import java.io.IOException;
import java.math.BigDecimal;

import javax.crypto.IllegalBlockSizeException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/cart")
public class AppleWebServer extends HttpServlet {

    private static final long serialVersionUID = -5266194719340643127L;
    private final String TYPE = "json";
    private static JsonConverter converter = new JsonConverter();
    private static Cryptographer cryptographer = new Cryptographer();

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie userCookie = findCartCookie(request);
        if (userCookie != null) {
            String decryptedCookieValue = "";
            try {
                decryptedCookieValue = cryptographer.decrypt(userCookie.getValue());
                sendResponse(response, 200, decryptedCookieValue);
            } catch (IllegalBlockSizeException e) {
                sendResponse(response, 400, "{\"Response\" : \"Invalid Cookie\"}");
            }
        } else {
            sendResponse(response, 400, "{\"Response\" : \"Cart is empty\"}");
        }
    }

    /**
     * @throws IOException
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String name = request.getParameter("name");
        String method = request.getParameter("method");
        String quantityString = request.getParameter("q");
        int quantity = convertQuantity(quantityString);
        BigDecimal price = ItemDatabase.takeItemPrice(name);

        Cart cart = null;

        if (verifyRequestParameters(method, quantity, price)) {
            Cookie userCookie = findCartCookie(request);
            if (userCookie != null) {
                String decryptedCookieValue = "";
                try {
                    decryptedCookieValue = cryptographer.decrypt(userCookie.getValue());
                    cart = converter.toObject(decryptedCookieValue);
                } catch (IllegalBlockSizeException e) {
                    sendResponse(response, 400, "{\"Response\" : \"Invalid Cookie\"}");
                }
            } else {
                cart = new Cart();
            }
            updateShoppingCart(cart, method, name, quantity, price);
            String cartJson = converter.toJson(cart);
            bakeCookie(response, cartJson);
            sendResponse(response, 200, cartJson);
        } else {
            sendResponse(response, 400, "{\"Response\" : \"Invalid parameters\"}");
        }
    }

    public int convertQuantity(String quantity) {
        try {
            return Integer.parseInt(quantity);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    public Cookie findCartCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase("cart")) {
                    return cookie;
                }
            }
        }
        return null;
    }

    private void bakeCookie(HttpServletResponse response, String body) {
        String encryptedBody = cryptographer.encrypt(body);
        Cookie responseCookie = new Cookie("cart", encryptedBody);
        responseCookie.setMaxAge(15 * 60);
        response.addCookie(responseCookie);
    }

    public boolean verifyRequestParameters(String method, int quantity, BigDecimal price) {
        if (price != null) {
            if (quantity > 0 && ("add".equalsIgnoreCase(method) || "update".equalsIgnoreCase(method))) {
                return true;
            }
            if ("delete".equalsIgnoreCase(method)) {
                return true;
            }
        }
        return false;
    }

    private void updateShoppingCart(Cart cart, String method, String name, int quantity, BigDecimal price) {
        if ("add".equalsIgnoreCase(method)) {
            cart.addItem(name, quantity, price);
        } else if ("update".equalsIgnoreCase(method)) {
            cart.updateItem(name, quantity);
        } else if ("delete".equalsIgnoreCase(method)) {
            cart.deleteItem(name);
        }
    }

    private void sendResponse(HttpServletResponse response, int code, String message) {
        response.setStatus(code);
        response.setHeader("Content-type", "application/" + TYPE);
        try {
            response.getOutputStream().println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
