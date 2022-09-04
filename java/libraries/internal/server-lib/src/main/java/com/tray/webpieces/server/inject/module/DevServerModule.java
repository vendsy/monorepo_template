package com.tray.webpieces.server.inject.module;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.matcher.Matchers;

import com.tray.webpieces.server.inject.listener.SingletonListener;

public class DevServerModule implements Module {

    @Override
    public void configure(Binder binder) {

        binder.bindListener(Matchers.any(), new SingletonListener("com.tray."));

    }

}
