package com.tray.webpieces.server.util;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.tray.api.internal.TrayService;
import com.tray.api.internal.TrayServiceConfig;
import com.tray.api.internal.TrayServiceInfo;

@Singleton
public class TrayServiceInfoImpl implements TrayServiceInfo {

    private final TrayServiceConfig config;
    private String version;

    @Inject
    public TrayServiceInfoImpl(final TrayServiceConfig config) {
        this.config = config;
    }

    @Override
    public TrayService getService() {
        return config.getService();
    }

    @Override
    public String getAccountId() {
        return AWSMetadata.getAccountId();
    }

    @Override
    public String getClusterId() {
        return AWSMetadata.getClusterId();
    }

    @Override
    public String getTaskId() {
        return AWSMetadata.getTaskId();
    }

    @Override
    public String getRegion() {
        return AWSMetadata.getRegion();
    }

    @Override
    public String getAvailabilityZone() {
        return AWSMetadata.getAvailabilityZone();
    }

    @Override
    public String getVersion() {

        if(version != null) {
            return version;
        }

        version = getClass().getPackage().getImplementationVersion();

        return version;

    }

}
