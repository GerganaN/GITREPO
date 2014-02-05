/*
 * $$ Gergana Nikolova, Veselin Aleksandrov, Atanas Kereziev, Stanislav Burov$$
 *
 * Copyright <2014> Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */
package com.skrill.interns.shoppingcartapi;

import java.math.BigDecimal;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/cart")
public interface ShoppingCartInterface {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Cart viewCart(@QueryParam("sessionId") String sessionId);

    @GET
    @Path("products")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, BigDecimal> showProducts();

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Cart add(Item item, @QueryParam("sessionId") String sessionId);

    @POST
    @Path("update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Cart update(Item item, @QueryParam("sessionId") String sessionId);

    @POST
    @Path("delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Cart delete(Item item, @QueryParam("sessionId") String sessionId);
}
