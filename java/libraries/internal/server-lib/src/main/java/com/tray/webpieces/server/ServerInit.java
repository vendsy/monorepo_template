package com.tray.webpieces.server;

import java.lang.reflect.Field;
import java.security.Security;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.function.BiFunction;

import com.google.inject.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.webpieces.webserver.api.ServerConfig;

public class ServerInit {

    private static final Logger log = LoggerFactory.getLogger(ServerInit.class);

    public static void start(BiFunction<Module, ServerConfig, CompanyServer> createSvr) {
        try {

            init();

            CompanyServer server = createSvr.apply(null, createServerConfig());
            server.start();

            synchronized (ServerInit.class) {
                //wait forever so server doesn't shut down..
                ServerInit.class.wait();
            }
        } catch(Throwable e) {
            log.error("Failed to startup.  exiting jvm", e);
            System.exit(1); // should not be needed BUT some 3rd party libraries start non-daemon threads :(
        }
    }

    public static void init() {

        Security.setProperty("networkaddress.cache.ttl" , "30");

        String jdkVersion = System.getProperty("java.version");
        log.info("Starting Production Server under java version="+jdkVersion);
        if(log.isDebugEnabled()) {
            Map<Object, Object> properties = new TreeMap<>(System.getProperties());
            for(Map.Entry<Object,Object> property : properties.entrySet()) {
                log.debug("System property:: key:: " + property.getKey() + ":: value :: " + property.getValue());
            }
            try {
                Field f = Security.class.getDeclaredField("props");
                f.setAccessible(true);
                Map<Object,Object> allProps = new TreeMap<>((Properties)f.get(null)); // Static field, so null object.
                for(Map.Entry<Object,Object> property : allProps.entrySet()) {
                    log.debug("System security property:: key:: " + property.getKey() + ":: value :: " + property.getValue());
                }
            }
            catch(Exception e) {
                log.error("EXCEPTION::", e);
            }
        }

    }

    private static ServerConfig createServerConfig() {
        ServerConfig config = new ServerConfig(true);
        return config;
    }

}
