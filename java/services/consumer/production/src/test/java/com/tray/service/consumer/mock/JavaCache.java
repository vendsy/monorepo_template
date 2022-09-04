package com.tray.service.consumer.mock;

import java.io.File;

import org.webpieces.util.file.FileFactory;

public class JavaCache {

	public static File getCacheLocation() {
		return FileFactory.newCacheLocation("ordersCache/precompressedFiles");
	}
	
}
