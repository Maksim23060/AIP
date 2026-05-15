package main.com.gateway.proxy;

import main.com.gateway.model.Request;
import main.com.gateway.model.Response;

public interface Service {
    Response execute(Request request);
}