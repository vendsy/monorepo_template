package com.tray.service.consumer.base;

import javax.inject.Singleton;

import com.tray.api.internal.TrayService;
import com.tray.api.internal.TrayServiceAddress;
import com.tray.api.internal.TrayServiceConfig;
import com.tray.api.internal.order.OrdersApi;

import com.google.inject.Provides;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webpieces.ctx.api.ApplicationContext;
import org.webpieces.microsvc.client.api.HttpsConfig;
import org.webpieces.microsvc.client.api.RESTClientCreator;
import org.webpieces.router.api.extensions.SimpleStorage;
import org.webpieces.router.api.extensions.Startable;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import org.webpieces.util.context.ClientAssertions;
import com.tray.webpieces.server.storage.NullSimpleStorage;

public class GuiceModule implements Module {

	private static final Logger log = LoggerFactory.getLogger(GuiceModule.class);
	
	//This is where you would put the guice bindings you need though generally if done
	//right, you won't have much in this file.
	
	//If you need more Guice Modules as you want to scale, just modify ServerMeta which returns
	//the list of all the Guice Modules in your application
	@SuppressWarnings("rawtypes")
	@Override
	public void configure(Binder binder) {
		
		log.info("running module");

		binder.bind(TrayServiceConfig.class).toInstance(new TrayServiceConfig(TrayService.CONSUMER));

		//all modules have access to adding their own Startable objects to be run on server startup
		Multibinder<Startable> startableBinder = Multibinder.newSetBinder(binder, Startable.class);

	    //Must bind a SimpleStorage for plugins to read/save data and render their html pages
	    binder.bind(SimpleStorage.class).to(NullSimpleStorage.class).asEagerSingleton();
	    
	    //since GlobalAppContext is a singleton, ApplicationContext will be to and will be the same
		binder.bind(ApplicationContext.class).to(GlobalAppContext.class).asEagerSingleton();
		binder.bind(ClientAssertions.class).to(ClientAssertionsImpl.class);
		binder.bind(HttpsConfig.class).toInstance(new HttpsConfig(false));
	}

	@Provides
	@Singleton
	public OrdersApi createOrdersApi(RESTClientCreator clientCreator, TrayServiceAddress serviceAddress) {
		return clientCreator.createClient(OrdersApi.class, serviceAddress.getOrdersHost());
	}

}
