package com.tray.webpieces.server.dev;

import com.google.inject.Module;

import org.webpieces.webserver.api.ServerConfig;

import com.tray.webpieces.server.CompanyServer;

public abstract class CompanyProdServerForIDE extends TrayAbstractDevServer {
    private final CompanyServer server;

    public CompanyProdServerForIDE(String name, boolean usePortZero) {
        super(name, usePortZero);
        this.server = createServer(null, serverConfig, args);
    }

    protected abstract CompanyServer createServer(Module platformOverrides, ServerConfig config, String ... args);

    @Override
    public final void start() {
        server.start();
    }

    public final void stop() {
        server.stop();
    }

}
