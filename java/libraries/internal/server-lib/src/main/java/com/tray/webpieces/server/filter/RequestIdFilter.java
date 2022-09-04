package com.tray.webpieces.server.filter;

import javax.inject.Inject;

import org.webpieces.ctx.api.RouterRequest;
import org.webpieces.router.api.controller.actions.Action;
import org.webpieces.router.api.routes.MethodMeta;
import org.webpieces.router.api.routes.RouteFilter;
import org.webpieces.util.filters.Service;
import org.webpieces.util.futures.XFuture;

import com.tray.webpieces.server.util.RequestIdGenerator;

public class RequestIdFilter extends RouteFilter<Void> {

    private final RequestIdGenerator requestIdGenerator;

    @Inject
    public RequestIdFilter(RequestIdGenerator requestIdGenerator) {
        this.requestIdGenerator = requestIdGenerator;
    }

    @Override
    public void initialize(Void initialConfig) {

    }

    @Override
    public XFuture<Action> filter(MethodMeta meta, Service<MethodMeta, Action> nextFilter) {

        RouterRequest request = meta.getCtx().getRequest();
        String requestHeader = request.getSingleHeaderValue(CompanyHeaders.REQUEST_ID.getHeaderName());
        String requestId;

        if(requestHeader != null) {
            requestId = requestHeader;
        } else {
            requestId = requestIdGenerator.generate().toString();
        }

        request.setRequestState(CompanyHeaders.REQUEST_ID, requestId);

        return nextFilter.invoke(meta);
    }

}
