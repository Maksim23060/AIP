package main.com.gateway.proxy;

import main.com.gateway.model.Request;
import main.com.gateway.model.Response;
import main.com.gateway.model.ServiceInstance;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceProxy implements Service {
    private final Map<Request, Response> cache = new ConcurrentHashMap<>();
    private final Map<String, Service> realServices = new ConcurrentHashMap<>();

    public Response callService(ServiceInstance instance, Request request) {
        // Проверяем кэш
        Response cached = getFromCache(request);
        if (cached != null) return cached;

        // Вызываем реальный сервис
        Service real = realServices.computeIfAbsent(instance.getUrl(), url -> new RealService(url));
        instance.incrementConnections();
        try {
            Response response = real.execute(request);
            putToCache(request, response);
            return response;
        } finally {
            instance.decrementConnections();
        }
    }

    public Response getFromCache(Request request) {
        return cache.get(request);
    }

    private void putToCache(Request request, Response response) {
        if (response.getStatusCode() == 200) {
            cache.put(request, response);
        }
    }

    @Override
    public Response execute(Request request) {
        throw new UnsupportedOperationException("Use callService with ServiceInstance");
    }
}