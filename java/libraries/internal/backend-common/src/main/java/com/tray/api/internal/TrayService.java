package com.tray.api.internal;

public enum TrayService {

    CONSUMER("consumer", 8000),
    ORDERS("orders", 8010);

    private final String serviceName;
    private final int devHttpPort;

    TrayService(final String serviceName, final int devHttpPort) {
        this.devHttpPort = devHttpPort;
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public int getDevHttpPort() {
        return devHttpPort;
    }

    public int getDevHttpsPort() {
        return devHttpPort + 1000;
    }

}
