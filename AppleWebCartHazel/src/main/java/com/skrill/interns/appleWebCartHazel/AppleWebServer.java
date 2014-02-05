package com.skrill.interns.appleWebCartHazel;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hazelcast.client.ClientConfig;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;

@WebServlet("/cart")
public class AppleWebServer extends HttpServlet {

    private static final long serialVersionUID = -5266194719340643127L;
    private final String TYPE = "json";
    private static JsonConverter converter = new JsonConverter();
    private static Map<String, String> sessions;
    private final int cookieMaxAge = 30 * 60;

    static {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.getGroupConfig().setName("session_cache").setPassword("mbsession");
        clientConfig.addAddress("10.32.18.39", "10.32.18.55", "10.32.18.71", "10.32.18.87", "10.32.18.103",
                "10.32.18.40", "10.32.18.56", "10.32.18.72", "10.32.18.88", "10.32.18.104");
        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        sessions = client.getMap("appleHazel");
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie userCookie = findSessionCookie(request);
        if (userCookie != null) {
            String sessionId = userCookie.getValue();
            String cartBody = sessions.get(sessionId);
            if (cartBody != null) {
                sendResponse(response, 200, cartBody);
            } else {
                sendResponse(response, 408, "{\"Response\" : \"Invalid session ID\"}");
            }
        } else {
            sendResponse(response, 200, "{\"Response\" : \"Cart is empty\"}");
        }
    }

    /**
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
        String sessionId = "";

        if (verifyRequestParameters(method, quantity, price)) {
            Cookie userCookie = findSessionCookie(request);
            if (userCookie != null) {
                sessionId = userCookie.getValue();
                String cartBody = sessions.get(sessionId);
                if (cartBody != null) {
                    cart = converter.toObject(cartBody);
                } else {
                    sendResponse(response, 408, "{\"Response\" : \"Invalid session ID\"}");
                }
            } else {
                sessionId = UUID.randomUUID().toString();
                cart = new Cart();
            }

            bakeCookie(response, sessionId);
            updateShoppingCart(cart, method, name, quantity, price);
            String cartJson = converter.toJson(cart);
            sessions.put(sessionId, cartJson);
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

    public Cookie findSessionCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase("sessionId")) {
                    return cookie;
                }
            }
        }
        return null;
    }

    private void bakeCookie(HttpServletResponse response, String value) {
        Cookie responseCookie = new Cookie("sessionId", value);
        responseCookie.setMaxAge(cookieMaxAge);
        response.addCookie(responseCookie);
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
