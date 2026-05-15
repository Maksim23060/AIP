package main.com.gateway.exception;


public class ServiceUnavailableException extends GatewayException {
    public ServiceUnavailableException(String message) {
        super(message);
    }
}