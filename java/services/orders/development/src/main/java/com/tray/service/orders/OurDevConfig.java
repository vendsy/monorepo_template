package com.tray.service.orders;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.tray.api.internal.TrayService;
import com.tray.service.orders.db.DbSettingsInMemory;
import com.tray.service.orders.service.RemoteService;
import com.tray.webpieces.server.dev.DevConfig;

public class OurDevConfig implements DevConfig {

	@Override
	public String[] getExtraArguments() {
		return null;
	}

	@Override
	public String getHibernateSettingsClazz() {
		return DbSettingsInMemory.class.getName();
	}

	@Override
	public int getHttpPort() {
		return TrayService.ORDERS.getDevHttpPort();
	}

	public int getHttpsPort() {
		return TrayService.ORDERS.getDevHttpsPort();
	}

	public Module getDevelopmentOverrides() {
		return new LocalhostOverrides();
	}

	private static class LocalhostOverrides implements Module {

		@Override
		public void configure(Binder binder) {
			binder.bind(RemoteService.class).to(LocalRemoteSvcSimulator.class);
		}

	}
}
