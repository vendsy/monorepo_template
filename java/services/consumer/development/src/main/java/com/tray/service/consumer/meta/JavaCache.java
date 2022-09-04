package com.tray.service.consumer.meta;

import org.webpieces.compiler.api.CompileConfig;
import org.webpieces.util.file.VirtualFile;

public class JavaCache {

	public static VirtualFile getByteCache() {
		return CompileConfig.getHomeCacheDir("ordersCache/devserver-bytecode");
	}
}
