package com.tray.service.consumer.meta;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.inject.Module;
import com.google.inject.util.Modules;

import org.webpieces.router.api.plugins.Plugin;
import org.webpieces.router.api.routes.Routes;
import org.webpieces.router.api.routes.WebAppConfig;
import org.webpieces.router.api.routes.WebAppMeta;

import com.tray.service.consumer.OurDevConfig;

import com.tray.service.consumer.base.ProdServerMeta;

public class DevServerMeta implements WebAppMeta {

	private ProdServerMeta prodMeta = new ProdServerMeta();
	
	@Override
	public void initialize(WebAppConfig pluginConfig) {
		prodMeta.initialize(pluginConfig);
	}
	
	@Override
	public List<Module> getGuiceModules() {
		List<Module> prod = prodMeta.getGuiceModules();
		Module localOverrides = new OurDevConfig().getDevelopmentOverrides();
		Module finalModule = Modules.override(prod).with(localOverrides);
		return Lists.newArrayList(finalModule);
	}

	@Override
	public List<Routes> getRouteModules() {
		return prodMeta.getRouteModules();
	}

	@Override
	public List<Plugin> getPlugins() {
		List<Plugin> prodPlugins = prodMeta.getPlugins();
		List<Plugin> devPlugins = Lists.newArrayList(

		);

		prodPlugins.addAll(devPlugins);
		
		return prodPlugins;
	}

}
