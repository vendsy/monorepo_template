package com.tray.webpieces.server.filter;

import org.webpieces.microsvc.api.MicroSvcHeader;
import org.webpieces.util.context.PlatformHeaders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HeaderContextList {
    public List<PlatformHeaders> listHeaderCtxPairs() {
        List<PlatformHeaders> list = new ArrayList<>();
        CompanyHeaders[] values = CompanyHeaders.values();
        MicroSvcHeader[] values1 = MicroSvcHeader.values();
        list.addAll(Arrays.asList(values));
        list.addAll(Arrays.asList(values1));
        return list;
    }
}
