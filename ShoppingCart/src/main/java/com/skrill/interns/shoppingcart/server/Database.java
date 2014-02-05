/*
 * $$ Gergana Nikolova, Veselin Aleksandrov, Atanas Kereziev, Stanislav Burov$$
 *
 * Copyright <2014> Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */
package com.skrill.interns.shoppingcart.server;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

import com.hazelcast.client.ClientConfig;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.skrill.interns.shoppingcartapi.Cart;

public class Database {
    private static Map<String, String> sessions;
    private static ObjectMapper objectMapper;

    static {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.getGroupConfig().setName("session_cache").setPassword("mbsession");
        clientConfig.addAddress("10.32.18.39", "10.32.18.55", "10.32.18.71", "10.32.18.87", "10.32.18.103",
                "10.32.18.40", "10.32.18.56", "10.32.18.72", "10.32.18.88", "10.32.18.104");
        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        sessions = client.getMap("appleHazel");

        ObjectMapper mapper = new ObjectMapper();
        AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
        mapper.setSerializationConfig(mapper.getSerializationConfig().withAnnotationIntrospector(introspector));
        mapper.setDeserializationConfig(mapper.getDeserializationConfig().withAnnotationIntrospector(introspector));
        objectMapper = mapper;
    }

    public static Cart get(String sessionId) {
        Cart cart = null;
        if (sessionId != null) {
            String cartBody = sessions.get(sessionId);
            if (cartBody != null) {
                cart = jsonToObject(sessions.get(sessionId));
            }
        }
        return cart;
    }

    public static void put(String sessionId, Cart cart) {
        String cartJson = objectToJson(cart);
        sessions.put(sessionId, cartJson);
    }

    public static String objectToJson(Cart cart) {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(cart);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static Cart jsonToObject(String json) {
        Cart cart = null;
        try {
            cart = objectMapper.readValue(json, Cart.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cart;
    }

    public static String ProductsDatabaseToJson(Map<String, BigDecimal> database) {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(database);
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
