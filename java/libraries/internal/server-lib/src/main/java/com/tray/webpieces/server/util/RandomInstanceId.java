package com.tray.webpieces.server.util;

import java.util.UUID;

public class RandomInstanceId {

	public static String generate() {
		String instanceId = UUID.randomUUID().toString();
		String s1 = instanceId.substring(0, 3);
		String s2 = instanceId.substring(3, 6);
		return s1+"-"+s2; //human readable instance id
	}
}
