package com.tray.webpieces.server.filter;

import org.webpieces.util.context.PlatformHeaders;

public enum CompanyHeaders implements PlatformHeaders {
    CUSTOMER_ID("X-Tray-CustomerId", "customerId"),
    REQUEST_ID("X-Tray-RequestId", "requestId"),
    NOT_INLOGS("X-Tray-Example", null),
    NOT_TRANSFERRED(null, "exampleLogged")
    ;

    private String header;
    private String mdcName;

    private CompanyHeaders(String header, String mdcName) {
        this.header = header;
        this.mdcName = mdcName;
    }
    @Override
    public String getHeaderName() {
        return header;
    }

    @Override
    public String getLoggerMDCKey() {
        return mdcName;
    }

    @Override
    public boolean isWantLogged() {
        return mdcName != null;
    }

    @Override
    public boolean isWantTransferred() {
        return header != null;
    }
}
