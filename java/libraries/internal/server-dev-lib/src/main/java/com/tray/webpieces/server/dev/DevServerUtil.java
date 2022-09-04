package com.tray.webpieces.server.dev;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.function.Supplier;

public class DevServerUtil {

	private static final Logger log = LoggerFactory.getLogger(DevServerUtil.class);

	public static void start(Supplier<TrayAbstractDevServer> function) {
		// assume SLF4J is bound to logback in the current environment
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		// print logback's internal status
		StatusPrinter.print(lc);

		URL resource = DevServerUtil.class.getResource("/logback.xml");
		if(resource == null) {
			throw new IllegalStateException("You ran into Intellij bug.  Add a newline to build.gradle and refresh gradle in Intellij to fix the bug");
		}

		try {
			String version = System.getProperty("java.version");
			TrayAbstractDevServer server = function.get();
			log.info("Starting "+server.getClass().getSimpleName()+" under java version="+version);

			server.start();
			
			//Since we typically use only 3rd party libraries with daemon threads, that means this
			//main thread is the ONLY non-daemon thread letting the server keep running so we need
			//to block it and hold it up from exiting.  Modify this to release if you want an ability
			//to remotely shutdown....
			synchronized(TrayAbstractDevServer.class) {
				TrayAbstractDevServer.class.wait();
			}
		} catch(Throwable e) {
			log.error("Failed to startup.  exiting jvm. msg="+e.getMessage(), e);
			System.exit(1); // should not be needed BUT some 3rd party libraries start non-daemon threads :(
		}		
	}
}
