/*
 * $$ Gergana Nikolova, Veselin Aleksandrov, Atanas Kereziev, Stanislav Burov$$
 *
 * Copyright <2014> Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */
package com.skrill.interns.client;

import java.io.IOException;
import java.util.Scanner;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ClientResponseFailure;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJacksonProvider;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import com.skrill.interns.shoppingcartapi.Cart;
import com.skrill.interns.shoppingcartapi.Item;
import com.skrill.interns.shoppingcartapi.ServiceError;
import com.skrill.interns.shoppingcartapi.ShoppingCartInterface;

public class Client {

    static String sessionId;
    static Cart cart;
    static ShoppingCartInterface service;
    static ObjectMapper mapper = new ObjectMapper();
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        ResteasyProviderFactory.setRegisterBuiltinByDefault(false);

        ResteasyProviderFactory providerFactory = ResteasyProviderFactory.getInstance();
        providerFactory.registerProvider(ResteasyJacksonProvider.class);
        providerFactory.addMessageBodyReader(ResteasyJacksonProvider.class);

        String uri = "http://localhost:8080/ShoppingCart";
        service = ProxyFactory.create(ShoppingCartInterface.class, uri);

        String method, answer;
        Item item = null;

        do {
            method = getMethodFromConsole().toLowerCase();
            if ("products".equalsIgnoreCase(method)) {
                System.out.println(service.showProducts());
            } else if ("view".equalsIgnoreCase(method)) {
                cart = service.viewCart(sessionId);
                if (cart != null) {
                    System.out.println(objectToJson());
                    sessionId = cart.getCartId();
                }
            } else {
                item = getItemFromConsole(method);
                updateCart(item, method);
                if (cart != null) {
                    System.out.println(objectToJson());
                    sessionId = cart.getCartId();
                }
            }

            System.out.print("Do you wish to do anything else? (Y/N) ");
            answer = input.nextLine();
        } while ("y".equalsIgnoreCase(answer));
        System.out.println("Thank you, come again!");
    }

    private static Item getItemFromConsole(String method) {

        System.out.print("NAME: ");
        String name = input.nextLine();
        int quantity = 0;
        if (!("delete".equalsIgnoreCase(method))) {
            System.out.print("QUANTITY: ");
            try {
                quantity = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Incorrect quantity entered! MUST be a NUMBER!");
            }
        }
        return new Item(name, quantity);
    }

    private static String getMethodFromConsole() {
        System.out.print("METHOD (add/update/delete/view/products): ");
        return input.nextLine().toLowerCase();
    }

    private static void updateCart(Item item, String method) {
        if ("view".equalsIgnoreCase(method)) {
            cart = service.viewCart(sessionId);
        } else if ("add".equalsIgnoreCase(method)) {
            try {
                cart = service.add(item, sessionId);
            } catch (ClientResponseFailure ex) {
                ClientResponse<?> clientResponse = ex.getResponse();
                ServiceError serviceError = clientResponse.getEntity(ServiceError.class);
                System.out.println(serviceError.getErrorMessage());
            }
        } else if ("update".equalsIgnoreCase(method)) {
            try {
                cart = service.update(item, sessionId);
            } catch (ClientResponseFailure ex) {
                ClientResponse<?> clientResponse = ex.getResponse();
                ServiceError serviceError = clientResponse.getEntity(ServiceError.class);
                System.out.println(serviceError.getErrorMessage());
            }
        } else if ("delete".equalsIgnoreCase(method)) {
            try {
                cart = service.delete(item, sessionId);
            } catch (ClientResponseFailure ex) {
                ClientResponse<?> clientResponse = ex.getResponse();
                ServiceError serviceError = clientResponse.getEntity(ServiceError.class);
                System.out.println(serviceError.getErrorMessage());
            }
        }
    }

    private static String objectToJson() {
        String json = null;
        try {
            json = mapper.writeValueAsString(cart);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
}
