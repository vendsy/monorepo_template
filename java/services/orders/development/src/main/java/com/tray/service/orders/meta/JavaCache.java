package com.tray.service.orders.meta;

import java.io.File;

import org.webpieces.compiler.api.CompileConfig;
import org.webpieces.util.file.FileFactory;
import org.webpieces.util.file.VirtualFile;

public class JavaCache {

	public static VirtualFile getByteCache() {
		return CompileConfig.getHomeCacheDir("ordersCache/devserver-bytecode");
	}
}
