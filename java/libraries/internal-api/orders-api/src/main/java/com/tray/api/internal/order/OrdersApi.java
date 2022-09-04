package com.tray.api.internal.order;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.tray.api.internal.order.request.SearchRequest;
import com.tray.api.internal.order.response.SearchResponse;
import org.webpieces.util.futures.XFuture;

import com.tray.api.internal.order.request.OrderRequest;
import com.tray.api.internal.order.response.OrderResponse;

@Path("/v1/orders")
public interface OrdersApi {

    @POST
    @Path("/create")
    XFuture<OrderResponse> order(OrderRequest request);

    @POST
    @Path("/search")
    XFuture<SearchResponse> search(SearchRequest request);
}
