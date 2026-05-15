package main.com.gateway.validator;


import main.com.gateway.exception.InvalidDataException;
import main.com.gateway.model.ServiceInstance;

public class ServiceInstanceValidator {
    public static void validate(ServiceInstance instance) throws InvalidDataException {
        if (instance.getId() == null || instance.getId().trim().isEmpty()) {
            throw new InvalidDataException("Service ID cannot be empty");
        }
        if (instance.getUrl() == null || !instance.getUrl().matches("^https?://.*$")) {
            throw new InvalidDataException("Invalid URL: " + instance.getUrl());
        }
        if (instance.getWeight() <= 0) {
            throw new InvalidDataException("Weight must be positive: " + instance.getWeight());
        }
    }
}