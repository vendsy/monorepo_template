package com.tray.webpieces.server.storage;

import java.util.HashMap;
import java.util.Map;
import org.webpieces.util.futures.XFuture;

import org.webpieces.router.api.extensions.SimpleStorage;

public class NullSimpleStorage implements SimpleStorage {

    @Override
    public XFuture<Void> save(String key, String subKey, String value) {
        return XFuture.completedFuture(null);
    }

    @Override
    public XFuture<Void> save(String key, Map<String, String> properties) {
        return XFuture.completedFuture(null);
    }

    @Override
    public XFuture<Map<String, String>> read(String key) {
        Map<String, String> props = new HashMap<>();
        return XFuture.completedFuture(props);
    }

    @Override
    public XFuture<Void> delete(String key) {
        return XFuture.completedFuture(null);
    }

    @Override
    public XFuture<Void> delete(String key, String subKey) {
        return XFuture.completedFuture(null);
    }

}
