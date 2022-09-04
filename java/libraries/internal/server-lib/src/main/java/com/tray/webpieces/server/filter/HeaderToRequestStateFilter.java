package com.tray.webpieces.server.filter;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webpieces.http2.api.dto.lowlevel.lib.Http2Header;
import com.webpieces.http2.api.dto.lowlevel.lib.Http2HeaderName;
import org.webpieces.ctx.api.Current;
import org.webpieces.router.api.controller.actions.Action;
import org.webpieces.router.api.routes.MethodMeta;
import org.webpieces.router.api.routes.RouteFilter;
import org.webpieces.util.context.PlatformHeaders;
import org.webpieces.util.filters.Service;
import org.webpieces.util.futures.XFuture;

import javax.inject.Inject;

public class HeaderToRequestStateFilter extends RouteFilter<Void> {

    private static final Logger log = LoggerFactory.getLogger(HeaderToRequestStateFilter.class);
    private HeaderContextList headerCtxList;

    @Inject
    public HeaderToRequestStateFilter(HeaderContextList headerCollector) {
        this.headerCtxList = headerCollector;
    }

    @Override
    public void initialize(Void initialConfig) {

    }

    @Override
    public XFuture<Action> filter(MethodMeta meta, Service<MethodMeta, Action> nextFilter) {

        //*********************************************
        // Move HeaderCollector to webpieces
        //*****************************************************
        List<PlatformHeaders> headers = headerCtxList.listHeaderCtxPairs();

        for (PlatformHeaders contextKey : headers) {
            if(!contextKey.isWantTransferred())
                continue;

            List<Http2Header> values = Current.request().originalRequest.getHeaderLookupStruct().getHeaders(contextKey.getHeaderName());

            if ((values == null) || values.isEmpty()) {
                continue;
            }

            if (values.size() > 1) {
                log.warn("Skipping header " + contextKey + ": multiple values (" + values + ")");
                continue;
            }

            String value = values.get(0).getValue();

            if (value != null) {
                Current.request().setRequestState(contextKey, value);
            }

        }

        final List<Http2Header> authHeaders = Current.request().originalRequest.getHeaderLookupStruct().getHeaders(Http2HeaderName.AUTHORIZATION);
        if (authHeaders != null) {
            if (authHeaders.size() == 1) {
                Current.request().setRequestState(Http2HeaderName.AUTHORIZATION.getHeaderName(), authHeaders.get(0).getValue());
            } else if (authHeaders.size() > 1) {
                log.warn("Skipping header " + Http2HeaderName.AUTHORIZATION.getHeaderName() + ": multiple values (" + authHeaders + ")");
            }
        }

        return nextFilter.invoke(meta);

    }

}
