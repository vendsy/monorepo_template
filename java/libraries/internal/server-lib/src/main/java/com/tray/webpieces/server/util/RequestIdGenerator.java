package com.tray.webpieces.server.util;

import java.util.UUID;

import com.tray.api.internal.util.UUID7;

public class RequestIdGenerator {

    public UUID generate() {
        return UUID7.generate();
    }

}
