package com.tray.api.internal;

public class TrayServiceConfig {

    private final TrayService service;

    public TrayServiceConfig(TrayService service) {
        this.service = service;
    }

    public TrayService getService() {
        return service;
    }

}
