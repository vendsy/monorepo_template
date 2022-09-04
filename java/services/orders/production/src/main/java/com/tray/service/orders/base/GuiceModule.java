package com.tray.service.orders.base;

import javax.inject.Singleton;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.webpieces.ctx.api.ApplicationContext;
import org.webpieces.ctx.api.extension.HtmlTagCreator;
import org.webpieces.microsvc.client.api.HttpsConfig;
import org.webpieces.microsvc.client.api.RESTClientCreator;
import org.webpieces.router.api.extensions.ObjectStringConverter;
import org.webpieces.router.api.extensions.SimpleStorage;
import org.webpieces.router.api.extensions.Startable;
import org.webpieces.util.context.ClientAssertions;

import com.tray.api.internal.TrayService;
import com.tray.api.internal.TrayServiceConfig;

import com.tray.service.orders.db.Education;
import com.tray.service.orders.db.Role;
import com.tray.service.orders.service.RemoteService;
import com.tray.service.orders.service.RemoteServiceSimulator;
import com.tray.service.orders.service.SimpleStorageImpl;
import com.tray.service.orders.service.SomeLibrary;
import com.tray.service.orders.service.SomeLibraryImpl;
import com.tray.service.orders.web.tags.MyHtmlTagCreator;

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

		binder.bind(TrayServiceConfig.class).toInstance(new TrayServiceConfig(TrayService.ORDERS));


		//all modules have access to adding their own Startable objects to be run on server startup
		Multibinder<Startable> uriBinder = Multibinder.newSetBinder(binder, Startable.class);
	    uriBinder.addBinding().to(PopulateDatabase.class);

		Multibinder<ObjectStringConverter> conversionBinder = Multibinder.newSetBinder(binder, ObjectStringConverter.class);
		conversionBinder.addBinding().to(Education.WebConverter.class);
		conversionBinder.addBinding().to(Role.WebConverter.class);

		Multibinder<HtmlTagCreator> htmlTagCreators = Multibinder.newSetBinder(binder, HtmlTagCreator.class);
		htmlTagCreators.addBinding().to(MyHtmlTagCreator.class);
		
	    binder.bind(SomeLibrary.class).to(SomeLibraryImpl.class);

	    //Must bind a SimpleStorage for plugins to read/save data and render their html pages
	    binder.bind(SimpleStorage.class).to(SimpleStorageImpl.class).asEagerSingleton();
	    
	    //since GlobalAppContext is a singleton, ApplicationContext will be to and will be the same
		binder.bind(ApplicationContext.class).to(GlobalAppContext.class).asEagerSingleton();

		binder.bind(HttpsConfig.class).toInstance(new HttpsConfig(false));

		binder.bind(ClientAssertions.class).to(ClientAssertionsImpl.class).asEagerSingleton();
	}

	@Provides
	@Singleton
	public RemoteService createRemoteSvc(RESTClientCreator factory) {
		return new RemoteServiceSimulator();

		//normally you would do something like this....
//		InetSocketAddress addr = new InetSocketAddress(9091);
//		return factory.createClient(RemoteService.class, addr);
	}

//	@Provides
//	@Singleton
//	public Http2Client createClient(MeterRegistry metrics) {
//
//		BackpressureConfig config = new BackpressureConfig();
//
//		//clients should NOT have backpressure or it could screw the server over when the server does not support backpresssure
//		config.setMaxBytes(null);
//
//		//This is an http1_1 client masquerading as an http2 client so we can switch to http2 faster when ready!!
//		Http2Client httpClient = Http2to11ClientFactory.createHttpClient("httpclient", 10, config, metrics);
//
//		return httpClient;
//
//	}
}
