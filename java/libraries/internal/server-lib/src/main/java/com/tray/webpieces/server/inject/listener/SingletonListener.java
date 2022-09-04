package com.tray.webpieces.server.inject.listener;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import javax.inject.Singleton;

public class SingletonListener implements TypeListener {

    private final String packagePrefix;

    public SingletonListener(String packagePrefix) {
        this.packagePrefix = packagePrefix;
    }

    @Override
    public <T> void hear(TypeLiteral<T> type, TypeEncounter<T> encounter) {

        Class<? super T> raw = type.getRawType();

        if (!raw.getPackageName().startsWith(packagePrefix)) {
            return;
        }

        Singleton annotation = type.getRawType().getAnnotation(Singleton.class);
        if (annotation == null) {
            encounter.addError("Injected class (" + raw.getName() + ") MUST be annotated with javax.inject.Singleton !");
        }

    }

}
