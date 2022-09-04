package com.tray.api.order;

import com.fasterxml.jackson.annotation.JsonCreator;

public class Order {

    private final int id;

    @JsonCreator
    public Order(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
