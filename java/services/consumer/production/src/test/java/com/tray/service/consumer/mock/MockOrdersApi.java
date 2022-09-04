package com.tray.service.consumer.mock;

import com.tray.api.internal.order.Order;
import com.tray.api.internal.order.OrdersApi;
import com.tray.api.internal.order.request.OrderRequest;
import com.tray.api.internal.order.request.SearchRequest;
import com.tray.api.internal.order.response.OrderResponse;
import com.tray.api.internal.order.response.SearchResponse;
import org.webpieces.util.futures.XFuture;

public class MockOrdersApi implements OrdersApi {
    @Override
    public XFuture<OrderResponse> order(OrderRequest request) {
        return XFuture.completedFuture(new OrderResponse(new Order(5)));
    }

    @Override
    public XFuture<SearchResponse> search(SearchRequest request) {
        return XFuture.completedFuture(new SearchResponse());
    }
}
