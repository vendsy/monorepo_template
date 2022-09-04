package com.tray.webpieces.server.dev;

import com.google.inject.Module;
import com.google.inject.util.Modules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.webpieces.compiler.api.CompileConfig;
import org.webpieces.devrouter.api.DevRouterModule;
import org.webpieces.webserver.api.ServerConfig;

import com.tray.webpieces.server.CompanyServer;
import com.tray.webpieces.server.inject.module.DevServerModule;

public abstract class CompanyDevelopmentServer extends TrayAbstractDevServer {

    private static final Logger log = LoggerFactory.getLogger(CompanyDevelopmentServer.class);

    private final CompanyServer server;

    public CompanyDevelopmentServer(String name, boolean usePortZero ) {

        super(name, usePortZero);

        //java source files encoding...
        CompileConfig devConfig = new CompileConfig(srcPaths, CompileConfig.getHomeCacheDir("xTrayCache/devserver-bytecode"));
        devConfig.setFailIfNotInSourcePaths("com.tray");
        Module platformOverrides = Modules.combine(
                new DevServerModule(),
                new DevRouterModule(devConfig)
        );

        this.server = createServer(platformOverrides, null, serverConfig, args);

    }

    protected abstract CompanyServer createServer(Module platformOverrides, Module appOverrides, ServerConfig config, String ... args);

    public final void start() {
        server.start();
    }

    public final void stop() {
        server.stop();
    }

}
