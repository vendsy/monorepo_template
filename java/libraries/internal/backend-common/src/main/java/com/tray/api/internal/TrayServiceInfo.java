package com.tray.api.internal;

public interface TrayServiceInfo {

    TrayService getService();

    String getAccountId();

    String getClusterId();

    String getTaskId();

    String getRegion();

    String getAvailabilityZone();

    String getVersion();

}