package com.tray.webpieces.server.dev;


public interface DevConfig {

    String[] getExtraArguments();

    String getHibernateSettingsClazz();

    int getHttpPort();

    int getHttpsPort();

}
