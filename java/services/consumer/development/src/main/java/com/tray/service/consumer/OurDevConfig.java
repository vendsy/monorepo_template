package com.tray.service.consumer;

import com.google.inject.Binder;
import com.google.inject.Module;

import org.webpieces.microsvc.client.api.HttpsConfig;

import com.tray.api.internal.TrayService;

import com.tray.webpieces.server.dev.DevConfig;

import java.rmi.server.RemoteServer;

public class OurDevConfig implements DevConfig {

	@Override
	public String[] getExtraArguments() {
		return null;
	}

	@Override
	public String getHibernateSettingsClazz() {
		return null;
	}

	@Override
	public int getHttpPort() {
		return TrayService.CONSUMER.getDevHttpPort();
	}

	@Override
	public int getHttpsPort() {
		return TrayService.CONSUMER.getDevHttpsPort();
	}

	public Module getDevelopmentOverrides() {
		return new LocalhostOverrides();
	}

	private static class LocalhostOverrides implements Module {

		@Override
		public void configure(Binder binder) {
			binder.bind(HttpsConfig.class).toInstance(new HttpsConfig(true));
		}

	}
}
