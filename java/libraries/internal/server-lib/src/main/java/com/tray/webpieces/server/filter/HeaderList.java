package com.tray.webpieces.server.filter;

import org.webpieces.util.context.PlatformHeaders;

//Move this to webpieces as well
public interface HeaderList {

    public PlatformHeaders[] getHeaders();
}
