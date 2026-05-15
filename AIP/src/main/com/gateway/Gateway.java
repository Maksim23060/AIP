package main.com.gateway;

import main.com.gateway.config.ConfigLoader;
import main.com.gateway.decorator.CompressionDecorator;
import main.com.gateway.decorator.LoggingDecorator;
import main.com.gateway.decorator.RetryDecorator;
import main.com.gateway.filter.*;
import main.com.gateway.model.Request;
import main.com.gateway.model.Response;
import main.com.gateway.model.ServiceInstance;
import main.com.gateway.observer.Event;
import main.com.gateway.observer.Subject;
import main.com.gateway.proxy.Service;
import main.com.gateway.proxy.ServiceProxy;
import main.com.gateway.strategy.RoutingStrategy;
import main.com.gateway.strategy.WeightedStrategy;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Gateway {
    private final FilterChain filterChain;
    private final Subject subject;
    private final ServiceProxy proxy;

    public Gateway(List<ServiceInstance> services, RoutingStrategy strategy, ServiceProxy proxy) {
        this.proxy = proxy;
        this.subject = new Subject();
        List<Filter> filters = Arrays.asList(
                new AuthFilter(),
                new RateLimitFilter(),
                new CacheFilter(proxy),
                new RouteFilter(services, strategy, proxy)
        );
        this.filterChain = new FilterChain(filters);
    }

    public Response handleRequest(Request request) {
        subject.notifyObservers(Event.REQUEST_RECEIVED, request);
        try {
            filterChain.reset();
            filterChain.proceed(request);
            Response response = filterChain.getFinalResponse();
            System.out.println("[Gateway] Final response from chain: " + (response != null ? response.getStatusCode() : "null"));
            if (response == null) {
                response = new Response(500, null, "Internal error");
            }
            subject.notifyObservers(Event.REQUEST_COMPLETED, response);
            return response;
        } catch (Exception e) {
            subject.notifyObservers(Event.REQUEST_FAILED, e);
            return new Response(500, null, "Gateway error: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        List<ServiceInstance> services = ConfigLoader.loadServices("services.txt");
        RoutingStrategy strategy = new WeightedStrategy();
        ServiceProxy proxy = new ServiceProxy();
        Gateway gateway = new Gateway(services, strategy, proxy);

        Request req = new Request("/api/test", "GET", Map.of("Authorization", "Bearer token"), null, "192.168.1.1");
        Response resp = gateway.handleRequest(req);
        System.out.println("Final response: " + resp);
    }
}