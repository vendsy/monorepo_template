package com.tray.api.internal;

import java.net.InetSocketAddress;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class TrayServiceAddress {

    private static final Logger log = LoggerFactory.getLogger(TrayServiceAddress.class);

    private InetSocketAddress consumerHost = new InetSocketAddress("localhost", TrayService.CONSUMER.getDevHttpsPort());
    private InetSocketAddress ordersHost = new InetSocketAddress("localhost", TrayService.ORDERS.getDevHttpsPort());

    @Inject
    public TrayServiceAddress(final TrayEnvironment env) {

        if(env == TrayEnvironment.PRODUCTION) {

            log.info("Using production hosts for clients");

            consumerHost = new InetSocketAddress("api.tray.com", 443);
            ordersHost = new InetSocketAddress("order.production.tray.com", 443);

        }
        else if (env == TrayEnvironment.PREPRODUCTION) {

            log.info("Using pre-production hosts for clients");

            consumerHost = new InetSocketAddress("api.preproduction.tray.com", 443);
            ordersHost = new InetSocketAddress("order.preproduction.tray.com", 443);

        }
        else if (env == TrayEnvironment.QA) {

            log.info("Using QA hosts for clients");

            consumerHost = new InetSocketAddress("api.qa.tray.com", 443);
            ordersHost = new InetSocketAddress("order.qa.tray.com", 443);

        }
        else if(env == TrayEnvironment.STAGING) {

            log.info("Using staging hosts for clients");

            consumerHost = new InetSocketAddress("api.staging.tray.com", 443);
            ordersHost = new InetSocketAddress("order.staging.tray.com", 443);

        }
        else if(env == TrayEnvironment.LOCAL) {
            log.info("Hosts defaulted to localhost");
            //do nothing, we like the defaults above
        }
        else {
            throw new IllegalStateException("Environment " + env + " not configured in TrayServiceAddress!");
        }

    }

    public InetSocketAddress getConsumerHost() {
        return consumerHost;
    }

    public InetSocketAddress getOrdersHost() {
        return ordersHost;
    }

    public InetSocketAddress getSocketAddress(TrayService service) {

        switch(service) {
            case CONSUMER: return getConsumerHost();
            case ORDERS: return getOrdersHost();
        }

        throw new IllegalArgumentException(service + " needs to be added to getSocketAddress");

    }

}
