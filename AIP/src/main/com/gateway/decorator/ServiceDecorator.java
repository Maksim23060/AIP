package main.com.gateway.decorator;

import main.com.gateway.model.Request;
import main.com.gateway.model.Response;
import main.com.gateway.proxy.Service;

public abstract class ServiceDecorator implements Service {
    protected final Service decorated;

    public ServiceDecorator(Service decorated) {
        this.decorated = decorated;
    }

    @Override
    public Response execute(Request request) {
        return decorated.execute(request);
    }
}