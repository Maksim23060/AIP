package main.com.gateway.filter;

import main.com.gateway.model.Request;
import main.com.gateway.model.Response;
import main.com.gateway.model.ServiceInstance;
import main.com.gateway.proxy.ServiceProxy;
import main.com.gateway.strategy.RoutingStrategy;

import java.util.List;

public class RouteFilter implements Filter {
    private final List<ServiceInstance> services;
    private final RoutingStrategy routingStrategy;
    private final ServiceProxy proxy;

    public RouteFilter(List<ServiceInstance> services, RoutingStrategy routingStrategy, ServiceProxy proxy) {
        this.services = services;
        this.routingStrategy = routingStrategy;
        this.proxy = proxy;
    }

    @Override
    public void handle(Request request, FilterChain chain) {
        ServiceInstance instance = routingStrategy.selectService(services);
        if (instance == null || !instance.isAlive()) {
            chain.setFinalResponse(new Response(503, null, "No available service"));
            return;
        }
        Response response = proxy.callService(instance, request);
        chain.setFinalResponse(response);
    }
}