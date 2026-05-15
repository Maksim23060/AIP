package main.com.gateway.filter;

import main.com.gateway.model.Request;

public interface Filter {
    void handle(Request request, FilterChain chain);
}