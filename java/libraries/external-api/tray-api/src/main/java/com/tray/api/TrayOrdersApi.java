package com.tray.api;

import java.util.concurrent.CompletableFuture;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.tray.api.order.request.OrderRequest;
import com.tray.api.order.response.OrderResponse;

@Path("/v1/order")
public interface TrayOrdersApi {

    @POST
    @Path("")
    CompletableFuture<OrderResponse> order(OrderRequest request);

}
