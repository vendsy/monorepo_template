package com.tray.service.consumer.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.webpieces.ctx.api.Current;
import org.webpieces.ctx.api.RouterHeader;
import org.webpieces.plugin.json.Jackson;
import com.tray.api.internal.TrayEnvironment;
import com.tray.api.internal.TrayServiceInfo;

@Singleton
public class TestController implements TestApi {

    private final TrayEnvironment environment;
    private final TrayServiceInfo serviceInfo;

    @Inject
    public TestController(TrayEnvironment environment, TrayServiceInfo serviceInfo) {
        this.environment = environment;
        this.serviceInfo = serviceInfo;
    }

    @Override
    public CompletableFuture<TestResponse> test(@Jackson TestRequest request) {

        Map<String, Object> instanceInfo = new LinkedHashMap<>();

        instanceInfo.put("serviceName", serviceInfo.getService().getServiceName());
        instanceInfo.put("accountId", serviceInfo.getAccountId());
        instanceInfo.put("environment", environment);
        instanceInfo.put("clusterId", serviceInfo.getClusterId());

        Map<String, String> environment = new TreeMap<>(System.getenv());

        Map<String,List<String>> headers = new TreeMap<>();

        for(Map.Entry<String, List<RouterHeader>> header : Current.request().getHeaders().entrySet()) {
            List<String> values = header.getValue().stream().map(v -> v.getValue()).collect(Collectors.toList());
            headers.put(header.getKey(), values);
        }

        return CompletableFuture.completedFuture(new TestResponse(
                instanceInfo,
                environment,
                headers,
                Current.request().requestState
        ));

    }

}