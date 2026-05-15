package main.com.gateway.strategy;

import main.com.gateway.model.ServiceInstance;

import java.util.List;

public class LeastConnectionsStrategy implements RoutingStrategy {
    @Override
    public ServiceInstance selectService(List<ServiceInstance> services) {
        return services.stream()
                .filter(ServiceInstance::isAlive)
                .min((a, b) -> Integer.compare(a.getActiveConnections(), b.getActiveConnections()))
                .orElse(null);
    }
}