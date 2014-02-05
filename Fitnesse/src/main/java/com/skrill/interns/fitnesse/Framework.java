package com.skrill.interns.fitnesse;

import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ClientResponseFailure;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJacksonProvider;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import com.skrill.interns.shoppingcartapi.Cart;
import com.skrill.interns.shoppingcartapi.CartItem;
import com.skrill.interns.shoppingcartapi.Item;
import com.skrill.interns.shoppingcartapi.ServiceError;
import com.skrill.interns.shoppingcartapi.ShoppingCartInterface;

public class Framework {

    private Cart cart;
    private String sessionId;
    private ShoppingCartInterface service;
    {
        ResteasyProviderFactory providerFactory = ResteasyProviderFactory.getInstance();
        providerFactory.registerProvider(ResteasyJacksonProvider.class);
        providerFactory.addMessageBodyReader(ResteasyJacksonProvider.class);
        service = ProxyFactory.create(ShoppingCartInterface.class, "http://localhost:8080/ShoppingCart");
    }

    public String addItem(String id, int quantity) {
        String result = null;
        try {
            cart = service.add(new Item(id, quantity), sessionId);
            result = "Item added successfully";
        } catch (ClientResponseFailure ex) {
            ClientResponse<?> clientResponse = ex.getResponse();
            ServiceError serviceError = clientResponse.getEntity(ServiceError.class);
            result = serviceError.getErrorMessage();
        }
        return result;
    }

    public int itemsCount() {
        return cart.getItems().size();
    }

    public int getItemQuantity(String id) {
        CartItem cartItem = cart.getItem(id);
        if (cartItem != null) {
            return cartItem.getQuantity();
        }
        return 0;
    }

    public boolean createCart() {
        cart = service.viewCart(null);
        sessionId = cart.getCartId();
        System.out.println("sessionId" + sessionId);
        if (cart.getCartId() != null) {
            return true;
        }
        return false;
    }

    public String updateItem(String id, int quantity) {
        String result = null;
        try {
            cart = service.update(new Item(id, quantity), sessionId);
            result = "Item updated successfully";
        } catch (ClientResponseFailure ex) {
            ClientResponse<?> clientResponse = ex.getResponse();
            ServiceError serviceError = clientResponse.getEntity(ServiceError.class);
            System.out.println(serviceError.getErrorMessage());
            result = serviceError.getErrorMessage();
        }
        return result;
    }

    public String deleteItem(String id) {
        String result = null;
        try {
            cart = service.delete(new Item(id, 0), sessionId);
            result = "Item deleted successfully";
        } catch (ClientResponseFailure ex) {
            ClientResponse<?> clientResponse = ex.getResponse();
            ServiceError serviceError = clientResponse.getEntity(ServiceError.class);
            result = serviceError.getErrorMessage();
        }
        return result;
    }

    public String getSessionId() {
        return sessionId;
    }
}
