package com.tray.service.orders;

import com.tray.service.orders.service.*;
import org.webpieces.util.futures.XFuture;

public class LocalRemoteSvcSimulator implements RemoteService {
    @Override
    public XFuture<FetchValueResponse> fetchValue(FetchValueRequest request) {
        return null;
    }

    @Override
    public XFuture<SendDataResponse> sendData(SendDataRequest num) {
        return null;
    }
}
