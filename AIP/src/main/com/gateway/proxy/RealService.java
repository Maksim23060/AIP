package main.com.gateway.proxy;

import main.com.gateway.model.Request;
import main.com.gateway.model.Response;

import java.util.Map;

public class RealService implements Service {
    private final String url;

    public RealService(String url) {
        this.url = url;
    }

    @Override
    public Response execute(Request request) {
        System.out.println("Calling real service at " + url);
        return new Response(200, Map.of("Content-Type", "application/json"), "{\"result\":\"ok\"}");
    }
}