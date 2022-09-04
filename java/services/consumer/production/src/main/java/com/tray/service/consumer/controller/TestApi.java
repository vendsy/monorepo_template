package com.tray.service.consumer.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.fasterxml.jackson.annotation.JsonCreator;

@Path("/v1/test")
public interface TestApi {

    @POST
    @Path("")
    CompletableFuture<TestResponse> test(TestRequest request);

    class TestRequest {

    }

    class TestResponse {

        private final Map<String, Object> instanceInfo;
        private final Map<String, String> environment;
        private final Map<String,List<String>> headers;
        private final Map<String, Object> requestState;

        @JsonCreator
        public TestResponse(Map<String, Object> instanceInfo, Map<String,String> environment, Map<String,List<String>> headers, Map<String,Object> requestState) {
            this.instanceInfo = instanceInfo;
            this.environment = environment;
            this.headers = headers;
            this.requestState = requestState;
        }

        public Map<String, Object> getInstanceInfo() {
            return instanceInfo;
        }

        public Map<String, String> getEnvironment() {
            return environment;
        }

        public Map<String,List<String>> getHeaders() {
            return headers;
        }

        public Map<String,Object> getRequestState() {
            return requestState;
        }

    }

}