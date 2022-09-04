package com.tray.service.consumer.framework;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.tray.api.internal.order.OrdersApi;
import com.tray.service.consumer.mock.MockOrdersApi;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.digitalforge.sneakythrow.SneakyThrow;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.webpieces.http2client.api.Http2Client;
import org.webpieces.microsvc.client.api.HttpsConfig;
import org.webpieces.microsvc.client.api.RESTClientCreator;
import org.webpieces.util.context.Context;
import org.webpieces.webserver.api.ServerConfig;
import org.webpieces.webserver.test.Asserts;
import org.webpieces.webserver.test.http2.CompanyApiTest;

import com.tray.api.TrayOrdersApi;

import com.tray.service.consumer.Server;
import com.tray.service.consumer.mock.JavaCache;

/**
 * These are working examples of tests that sometimes are better done with the BasicSeleniumTest example but are here for completeness
 * so you can test the way you would like to test
 *
 * @author dhiller
 *
 */
public class FeatureTest extends CompanyApiTest {

    private final static Logger log = LoggerFactory.getLogger(FeatureTest.class);
    private String[] args = { "-http.port=:0", "-https.port=:0" };

    protected TrayOrdersApi ordersApi;
    protected SimpleMeterRegistry metrics;

    protected MockOrdersApi mockOrdersApi = new MockOrdersApi();

    @BeforeEach
    public void setUp() throws InterruptedException, ClassNotFoundException, ExecutionException, TimeoutException {
        log.info("Setting up test");
        super.initialize();
        ordersApi = super.createRestClient(TrayOrdersApi.class);
    }

    @AfterEach
    public void tearDown() {
        //do not leak context between tests
        Context.clear();
    }

    @Override
    protected void startServer() {
        metrics = new SimpleMeterRegistry();
        Server webserver = new Server(getOverrides(metrics),new AppOverridesModule(),
                new ServerConfig(JavaCache.getCacheLocation()), args
        );
        webserver.start();

        serverHttpsAddr = new InetSocketAddress("localhost", webserver.getUnderlyingHttpsChannel().getLocalAddress().getPort());
        serverHttpAddr = new InetSocketAddress("localhost", webserver.getUnderlyingHttpChannel().getLocalAddress().getPort());
    }

    private class AppOverridesModule implements Module {
        @Override
        public void configure(Binder binder) {

            binder.bind(HttpsConfig.class).toInstance(new HttpsConfig(true));
            binder.bind(OrdersApi.class).toInstance(mockOrdersApi);
        }
    }

}
