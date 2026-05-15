package main.com.gateway.filter;

import main.com.gateway.model.Request;
import main.com.gateway.model.Response;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RateLimitFilter implements Filter {
    private static final int LIMIT = 10;
    private final Map<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();

    @Override
    public void handle(Request request, FilterChain chain) {
        String ip = request.getClientIp();
        int count = requestCounts.computeIfAbsent(ip, k -> new AtomicInteger(0)).incrementAndGet();
        if (count > LIMIT) {
            chain.setFinalResponse(new Response(429, Map.of("Content-Type", "text/plain"), "Too Many Requests"));
            return;
        }
        chain.proceed(request);
    }
}