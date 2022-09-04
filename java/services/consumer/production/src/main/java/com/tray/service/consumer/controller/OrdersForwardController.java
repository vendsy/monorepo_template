package com.tray.service.consumer.controller;

import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.tray.api.internal.order.request.SearchRequest;
import com.tray.api.internal.order.response.SearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.webpieces.plugin.json.Jackson;
import org.webpieces.util.futures.XFuture;

import com.tray.api.internal.order.OrdersApi;
import com.tray.api.internal.order.request.OrderRequest;
import com.tray.api.internal.order.response.OrderResponse;

@Singleton
public class OrdersForwardController implements OrdersApi {

    private static final Logger log = LoggerFactory.getLogger(OrdersForwardController.class);

    private final OrdersApi ordersClient;

    @Inject
    public OrdersForwardController(OrdersApi ordersClient) {
        this.ordersClient = ordersClient;
    }

    @Override
    public XFuture<OrderResponse> order(@Jackson OrderRequest request) {
        return ordersClient.order(request);
    }

    @Override
    public XFuture<SearchResponse> search(SearchRequest request) {
        return ordersClient.search(request);
    }

}
