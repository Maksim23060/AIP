package main.com.gateway.strategy;

import main.com.gateway.model.ServiceInstance;

import java.util.List;

public interface RoutingStrategy {
    ServiceInstance selectService(List<ServiceInstance> services);
}