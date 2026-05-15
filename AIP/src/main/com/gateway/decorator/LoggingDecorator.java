package main.com.gateway.decorator;

import main.com.gateway.model.Request;
import main.com.gateway.model.Response;
import main.com.gateway.proxy.Service;

import java.time.LocalDateTime;

public class LoggingDecorator extends ServiceDecorator {
    public LoggingDecorator(Service decorated) {
        super(decorated);
    }

    @Override
    public Response execute(Request request) {
        System.out.println(LocalDateTime.now() + " REQ: " + request);
        Response response = super.execute(request);
        System.out.println(LocalDateTime.now() + " RESP: " + response);
        return response;
    }
}