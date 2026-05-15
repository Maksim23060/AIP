package main.com.gateway.strategy;

import main.com.gateway.model.ServiceInstance;

import java.util.List;
import java.util.Random;

public class WeightedStrategy implements RoutingStrategy {
    private final Random random = new Random();

    @Override
    public ServiceInstance selectService(List<ServiceInstance> services) {
        int totalWeight = services.stream().filter(ServiceInstance::isAlive).mapToInt(ServiceInstance::getWeight).sum();
        if (totalWeight == 0) return null;
        int point = random.nextInt(totalWeight);
        int current = 0;
        for (ServiceInstance s : services) {
            if (!s.isAlive()) continue;
            current += s.getWeight();
            if (point < current) return s;
        }
        return null;
    }
}