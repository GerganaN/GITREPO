package com.skrill.interns.cart;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/view")
public class View extends CherryServlet {

    private static final long serialVersionUID = -1093885735399882106L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String responseMessage = jsonConverter.convertShopToJson(shop);
        respondToClient(response, responseMessage.toString(), 200, "json");
    }
}
