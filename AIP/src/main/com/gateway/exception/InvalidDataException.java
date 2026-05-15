package main.com.gateway.exception;

public class InvalidDataException extends GatewayException {
    public InvalidDataException(String message) {
        super(message);
    }
}