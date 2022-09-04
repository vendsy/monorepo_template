package com.tray.api.order.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderRequest {

    private final int id;

    @JsonCreator
    public OrderRequest(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
