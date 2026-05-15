package main.com.gateway.decorator;

import main.com.gateway.model.Request;
import main.com.gateway.model.Response;
import main.com.gateway.proxy.Service;

public class CompressionDecorator extends ServiceDecorator {
    public CompressionDecorator(Service decorated) {
        super(decorated);
    }

    @Override
    public Response execute(Request request) {
        Response response = super.execute(request);
        // Имитация сжатия тела ответа
        String compressedBody = "[compressed]" + response.getBody();
        return new Response(response.getStatusCode(), response.getHeaders(), compressedBody);
    }
}