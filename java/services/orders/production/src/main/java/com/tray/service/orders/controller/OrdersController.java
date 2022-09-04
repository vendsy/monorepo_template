package com.tray.service.orders.controller;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.tray.api.internal.order.request.SearchRequest;
import com.tray.api.internal.order.response.SearchResponse;
import com.tray.service.orders.service.RemoteService;
import com.tray.service.orders.service.SendDataRequest;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.webpieces.plugin.json.Jackson;
import org.webpieces.util.futures.XFuture;

import com.tray.api.internal.order.Order;
import com.tray.api.internal.order.OrdersApi;
import com.tray.api.internal.order.request.OrderRequest;
import com.tray.api.internal.order.response.OrderResponse;

@Singleton
public class OrdersController implements OrdersApi {

    private static final Logger log = LoggerFactory.getLogger(OrdersController.class);

    private final Counter counter;
    private RemoteService remoteService;

    @Inject
    public OrdersController(MeterRegistry metrics, RemoteService remoteService) {
        counter = metrics.counter("testCounter");
        this.remoteService = remoteService;
    }

    @Override
    public XFuture<OrderResponse> order(@Jackson OrderRequest request) {

        log.info("getOrder(id=" + request.getId() + ")");

        OrderResponse response = new OrderResponse();
        Order order = new Order();

        order.setId(request.getId());
        response.setOrder(order);

        return XFuture.completedFuture(new OrderResponse());

    }

    @Override
    public XFuture<SearchResponse> search(@Jackson SearchRequest request) {

        counter.increment();

        //so we can test out mocking remote services
        remoteService.sendData(new SendDataRequest(6)).join();

        SearchResponse resp = new SearchResponse();
        resp.setSearchTime(99);
        resp.getMatches().add("match1");
        resp.getMatches().add("match2");
        return XFuture.completedFuture(resp);
    }
}
