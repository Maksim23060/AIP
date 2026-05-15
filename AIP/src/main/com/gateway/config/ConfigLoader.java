package main.com.gateway.config;

import main.com.gateway.exception.InvalidDataException;
import main.com.gateway.model.ServiceInstance;
import main.com.gateway.validator.ServiceInstanceValidator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ConfigLoader {
    public static List<ServiceInstance> loadServices(String filePath) throws IOException {
        List<ServiceInstance> services = new ArrayList<>();
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            lines.forEach(line -> {
                try {
                    ServiceInstance instance = parseServiceLine(line);
                    ServiceInstanceValidator.validate(instance);
                    services.add(instance);
                } catch (InvalidDataException e) {
                    System.err.println("Skipping invalid line: " + line + " - " + e.getMessage());
                }
            });
        }
        return services;
    }

    private static ServiceInstance parseServiceLine(String line) throws InvalidDataException {
        String[] parts = line.split(",");
        if (parts.length < 3) {
            throw new InvalidDataException("Not enough fields");
        }
        String id = parts[0].trim();
        String url = parts[1].trim();
        int weight;
        try {
            weight = Integer.parseInt(parts[2].trim());
        } catch (NumberFormatException e) {
            throw new InvalidDataException("Invalid weight: " + parts[2]);
        }
        boolean alive = true;
        if (parts.length >= 4) {
            alive = Boolean.parseBoolean(parts[3].trim());
        }
        return new ServiceInstance(id, url, weight, alive);
    }
}