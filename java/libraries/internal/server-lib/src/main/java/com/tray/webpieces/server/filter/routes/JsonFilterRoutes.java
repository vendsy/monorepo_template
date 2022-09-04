package com.tray.webpieces.server.filter.routes;

import java.util.regex.Pattern;

import org.webpieces.plugin.json.JacksonCatchAllFilter;
import org.webpieces.plugin.json.JsonConfig;
import org.webpieces.router.api.routebldr.DomainRouteBuilder;
import org.webpieces.router.api.routebldr.RouteBuilder;
import org.webpieces.router.api.routes.FilterPortType;
import org.webpieces.router.api.routes.Routes;

import com.tray.webpieces.server.filter.HeaderToRequestStateFilter;
import com.tray.webpieces.server.filter.MDCFilter;
import com.tray.webpieces.server.filter.RequestIdFilter;

public class JsonFilterRoutes implements Routes {

    @Override
    public void configure(DomainRouteBuilder domainRouteBldr) {

        RouteBuilder builder = domainRouteBldr.getAllDomainsRouteBuilder();

        String regex = "com\\.tray\\.service\\.[a-z]+\\.controller\\..*";

        builder.setInternalErrorRoute("JsonErrorNotFoundController.internalError");
        builder.setPageNotFoundRoute("JsonErrorNotFoundController.notFound");

        builder.addPackageFilter(regex, RequestIdFilter.class, null, FilterPortType.ALL_FILTER, 100);
        builder.addPackageFilter(regex, HeaderToRequestStateFilter.class, null, FilterPortType.ALL_FILTER, 95);
        builder.addPackageFilter(regex, MDCFilter.class, null, FilterPortType.ALL_FILTER, 90);

    }

}
