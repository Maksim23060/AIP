package main.com.gateway.model;

import java.util.Map;
import java.util.Objects;

public class Request {
    private final String path;
    private final String method;
    private final Map<String, String> headers;
    private final String body;
    private final String clientIp;

    public Request(String path, String method, Map<String, String> headers, String body, String clientIp) {
        this.path = path;
        this.method = method;
        this.headers = headers;
        this.body = body;
        this.clientIp = clientIp;
    }

    public String getPath() { return path; }
    public String getMethod() { return method; }
    public Map<String, String> getHeaders() { return headers; }
    public String getBody() { return body; }
    public String getClientIp() { return clientIp; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return Objects.equals(path, request.path) &&
                Objects.equals(method, request.method) &&
                Objects.equals(headers, request.headers) &&
                Objects.equals(body, request.body) &&
                Objects.equals(clientIp, request.clientIp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, method, headers, body, clientIp);
    }

    @Override
    public String toString() {
        return "Request{path='" + path + "', method='" + method + "', clientIp='" + clientIp + "'}";
    }
}