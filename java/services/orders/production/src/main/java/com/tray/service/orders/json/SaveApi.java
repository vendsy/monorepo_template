package com.tray.service.orders.json;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.tray.api.internal.order.request.SearchRequest;
import com.tray.api.internal.order.response.SearchResponse;
import org.webpieces.util.futures.XFuture;

public interface SaveApi {

    @POST
    @Path("/search/item")
    public XFuture<SearchResponse> search(SearchRequest request);


}
