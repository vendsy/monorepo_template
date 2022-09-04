package com.tray.webpieces.server;

import java.util.Arrays;
import java.util.function.Function;

import com.google.inject.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import org.webpieces.nio.api.channels.TCPServerChannel;
import org.webpieces.webserver.api.ServerConfig;
import org.webpieces.webserver.api.WebpiecesServer;

import com.tray.webpieces.server.inject.module.MetricsModule;
import com.tray.webpieces.server.util.RandomInstanceId;

public abstract class CompanyServer {

    private static final Logger log = LoggerFactory.getLogger(CompanyServer.class);

    protected WebpiecesServer webServer;

    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    public static void main(Function<ServerConfig,CompanyServer> yourServer) {
        try {
            String version = System.getProperty("java.version");
            log.info("Starting Production Server under java version="+version);


            ServerConfig config = new ServerConfig(true);
            CompanyServer server2 = yourServer.apply(config);

            server2.start();

            synchronized (CompanyServer.class) {
                //wait forever so server doesn't shut down..
                CompanyServer.class.wait();
            }
        } catch(Throwable e) {
            log.error("Failed to startup.  exiting jvm", e);
            System.exit(1); // should not be needed BUT some 3rd party libraries start non-daemon threads :(
        }
    }

    public CompanyServer(String appName, Module platformOverrides, Module appOverrides, ServerConfig svrConfig, String ... args) {

        String base64Key = "8jV/83s2xwXa3kThASMqGYxrd6CuDT3zsVzyj+3BElwQ2l8MiR/qKLcQmoiey1TCLq/DzEX7AS9Wyklk/9TeQg==";  //This gets replaced with a unique key each generated project which you need to keep or replace with your own!!!

        log.info("Constructing WebpiecesServer with args="+Arrays.asList(args));

        //This is a special platform module, the only CORE module we pass in that can be overriden in platformOverrides as well.
        //If you have 100 microservers, a few of them may override this in platformOverrides for special cases or testing
        //You could pass in an instance id, but this works for now too...
        String instanceId = RandomInstanceId.generate();
        MetricsModule metricsModule = new MetricsModule(instanceId);

        webServer = new WebpiecesServer(appName, base64Key, metricsModule, platformOverrides, appOverrides, svrConfig, args);
    }

    public final void start() {
        webServer.start();
    }

    public final void stop() {
        webServer.stop();
    }

    public final TCPServerChannel getUnderlyingHttpChannel() {
        return webServer.getUnderlyingHttpChannel();
    }
    public final TCPServerChannel getUnderlyingHttpsChannel() {
        return webServer.getUnderlyingHttpsChannel();
    }

}
