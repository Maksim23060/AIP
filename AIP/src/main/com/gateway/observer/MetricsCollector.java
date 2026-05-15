package main.com.gateway.observer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MetricsCollector implements Observer {
    private final ConcurrentHashMap<Event, AtomicLong> counters = new ConcurrentHashMap<>();

    @Override
    public void update(Event event, Object data) {
        counters.computeIfAbsent(event, e -> new AtomicLong()).incrementAndGet();
        System.out.println("[METRICS] " + event + " count: " + counters.get(event).get());
    }
}
