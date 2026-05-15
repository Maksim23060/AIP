package main.com.gateway.filter;

import main.com.gateway.model.Request;
import main.com.gateway.model.Response;
import main.com.gateway.proxy.ServiceProxy;

public class CacheFilter implements Filter {
    private final ServiceProxy proxy;

    public CacheFilter(ServiceProxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public void handle(Request request, FilterChain chain) {
        Response cached = proxy.getFromCache(request);
        if (cached != null) {
            chain.setFinalResponse(cached);
        } else {
            chain.proceed(request);
        }
    }
}