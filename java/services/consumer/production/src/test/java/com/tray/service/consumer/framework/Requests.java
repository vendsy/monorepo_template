package com.tray.service.consumer.framework;

import com.tray.api.order.request.OrderRequest;

public class Requests {

    public static OrderRequest createOrderRequest() {
        OrderRequest request = new OrderRequest(123);
        return request;
    }

}
