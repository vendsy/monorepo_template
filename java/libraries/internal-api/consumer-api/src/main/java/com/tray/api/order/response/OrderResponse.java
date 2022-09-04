package com.tray.api.order.response;

import com.fasterxml.jackson.annotation.JsonCreator;

import com.tray.api.order.Order;

public class OrderResponse {

    private final Order order;

    @JsonCreator
    public OrderResponse(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

}
