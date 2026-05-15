package main.com.gateway.validator;


import main.com.gateway.exception.InvalidDataException;
import main.com.gateway.model.Request;

public class RequestValidator {
    public static void validate(Request request) throws InvalidDataException {
        if (request.getPath() == null || request.getPath().isEmpty()) {
            throw new InvalidDataException("Request path is empty");
        }
        if (request.getMethod() == null || (!request.getMethod().equals("GET") && !request.getMethod().equals("POST"))) {
            throw new InvalidDataException("Unsupported HTTP method: " + request.getMethod());
        }
    }
}