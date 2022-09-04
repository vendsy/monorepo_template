package com.tray.api.internal;

import java.util.HashMap;
import java.util.Map;

public enum TrayEnvironment {

    LOCAL("local"),
    STAGING("203345496293"),
    QA("324657675270"),
    PREPRODUCTION("204514101532"),
    PRODUCTION("351277392518");

    private static final Map<String, TrayEnvironment> BY_ACCOUNT_ID = new HashMap<>();

    static {
        for(TrayEnvironment env : TrayEnvironment.values()) {
            BY_ACCOUNT_ID.put(env.getAccountId(), env);
        }
    }

    private final String accountId;

    TrayEnvironment(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }

    public static TrayEnvironment byAccountId(String accountId) {

        if(accountId == null) {
            throw new NullPointerException("accountId is null");
        }

        TrayEnvironment env = BY_ACCOUNT_ID.get(accountId);

        if(env == null) {
            throw new IllegalArgumentException("No enum for accountId '" + accountId + "'");
        }

        return env;

    }

}
