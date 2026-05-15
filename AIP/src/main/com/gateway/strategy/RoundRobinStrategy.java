package main.com.gateway.strategy;

import main.com.gateway.model.ServiceInstance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinStrategy implements RoutingStrategy {
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public ServiceInstance selectService(List<ServiceInstance> services) {
        if (services.isEmpty()) return null;
        int index = counter.getAndIncrement() % services.size();
        return services.get(index);
    }
}