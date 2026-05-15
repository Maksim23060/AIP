package main.com.gateway.decorator;

import main.com.gateway.model.Request;
import main.com.gateway.model.Response;
import main.com.gateway.proxy.Service;

public class RetryDecorator extends ServiceDecorator {
    private final int maxRetries;

    public RetryDecorator(Service decorated, int maxRetries) {
        super(decorated);
        this.maxRetries = maxRetries;
    }

    @Override
    public Response execute(Request request) {
        Exception lastException = null;
        for (int i = 0; i < maxRetries; i++) {
            try {
                return super.execute(request);
            } catch (Exception e) {
                lastException = e;
                System.out.println("Retry " + (i+1) + " for " + request);
            }
        }
        throw new RuntimeException("Failed after " + maxRetries + " retries", lastException);
    }
}