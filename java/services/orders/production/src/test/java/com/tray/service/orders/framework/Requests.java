package com.tray.service.orders.framework;

import com.tray.api.internal.order.request.SearchRequest;

public class Requests {
    public static SearchRequest createSearchRequest() {
        SearchRequest req = new SearchRequest();
        req.setQuery("my query");
        return req;
    }
}
