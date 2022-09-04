package com.tray.webpieces.server.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.digitalforge.sneakythrow.SneakyThrow;

public final class AWSMetadata {

    private static final AtomicBoolean INITALIZED = new AtomicBoolean();
    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();
    private static final String BASE_URL = System.getenv("ECS_CONTAINER_METADATA_URI_V4");

    private static String accountId;
    private static String clusterId;
    private static String taskId;
    private static String region;
    private static String availabilityZone;
    private static String logGroup;
    private static String logStream;
    private static String gcpProjectId;

    public static void init() {

        if(!INITALIZED.compareAndSet(false, true)) {
            return;
        }

        if(BASE_URL == null) {
            clusterId = "local";
            taskId = "local";
            availabilityZone = "local";
            region = "local";
            accountId = "203345496293";
            logGroup = "local";
            logStream = "local";
            gcpProjectId = calcGcpProjectId();
            return;
        }

        try {

            HttpClient client = HttpClient.newHttpClient();

            JsonNode root = sendRequest(client, BASE_URL);
            JsonNode rootTask = sendRequest(client, BASE_URL + "/task");

            clusterId = rootTask.get("Cluster").asText();
            taskId = rootTask.get("TaskARN").asText();
            availabilityZone = rootTask.get("AvailabilityZone").asText();

            String[] csplit1 = clusterId.split(":");
            String[] csplit2 = csplit1[5].split("-");

            region = csplit1[3];
            accountId = csplit2[2];

            logGroup = root.get("LogOptions").get("awslogs-group").asText();
            logStream = root.get("LogOptions").get("awslogs-stream").asText();

            gcpProjectId = calcGcpProjectId();

        } catch(Exception ex) {
            throw SneakyThrow.sneak(ex);
        }

    }

    public static String getAccountId() {
        init();
        return accountId;
    }

    public static String getClusterId() {
        init();
        return clusterId;
    }

    public static String getTaskId() {
        init();
        return taskId;
    }

    public static String getRegion() {
        init();
        return region;
    }

    public static String getAvailabilityZone() {
        init();
        return availabilityZone;
    }

    public static String getLogGroup() {
        init();
        return logGroup;
    }

    public static String getLogStream() {
        init();
        return logStream;
    }

    public static String getGcpProjectId() {
        init();
        return gcpProjectId;
    }

    private static String calcGcpProjectId() {

        String accountId = getAccountId();

        switch(accountId) {
            case "203345496293": return "tray-staging";
            case "324657675270": return "tray-qa";
            case "204514101532": return "tray-preproduction";
            case "351277392518": return "tray-production";
        }

        throw new IllegalArgumentException("Unknown accountId " + accountId);

    }

    private static JsonNode sendRequest(HttpClient client, String url) throws Exception {

        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
            .GET()
            .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return MAPPER.readTree(response.body());

    }

}
