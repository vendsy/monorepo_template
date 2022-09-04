package com.tray.webpieces.server.inject.module;

import java.util.function.Supplier;
import javax.inject.Singleton;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.webpieces.http2client.api.Http2Client;
import org.webpieces.httpclientx.api.Http2to11ClientFactory;
import org.webpieces.nio.api.BackpressureConfig;
import org.webpieces.util.cmdline2.Arguments;

import com.tray.api.internal.TrayEnvironment;
import com.tray.api.internal.TrayServiceInfo;

import com.tray.webpieces.server.util.AWSMetadata;
import com.tray.webpieces.server.util.TrayServiceInfoImpl;

@Singleton
public class CompanyModule implements Module {

    private static final Logger log = LoggerFactory.getLogger(CompanyModule.class);

    private final Supplier<Boolean> isDocker;

    public CompanyModule(Arguments arguments) {
        this.isDocker = arguments.createOptionalArg("isDocker", "false", "Whether or not you are running in docker.", (str) -> Boolean.valueOf(str));
    }

    @Override
    public void configure(Binder binder) {

        AWSMetadata.init();

        binder.bind(TrayServiceInfo.class).to(TrayServiceInfoImpl.class);

    }

    @Provides
    @Singleton
    public TrayEnvironment provideEnvironment() {

        String accountId = AWSMetadata.getAccountId();

        if((accountId == null) || accountId.equals("local")) {
            return TrayEnvironment.LOCAL;
        }

        return TrayEnvironment.byAccountId(accountId);

    }

    @Provides
    @Singleton
    public Http2Client createClient(MeterRegistry metrics) {

        BackpressureConfig config = new BackpressureConfig();

        //clients should NOT have backpressure or it could screw the server over when the server does not support backpresssure
        config.setMaxBytes(null);

        //This is an http1_1 client masquerading as an http2 client so we can switch to http2 faster when ready!!
        Http2Client httpClient = Http2to11ClientFactory.createHttpClient("httpclient", 10, config, metrics);

        return httpClient;

    }

}
