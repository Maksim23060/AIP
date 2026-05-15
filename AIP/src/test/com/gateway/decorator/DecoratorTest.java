package test.com.gateway.decorator;

import main.com.gateway.decorator.CompressionDecorator;
import main.com.gateway.decorator.LoggingDecorator;
import main.com.gateway.decorator.RetryDecorator;
import org.testng.annotations.Test;
import main.com.gateway.model.Request;
import main.com.gateway.model.Response;
import main.com.gateway.proxy.Service;

import java.util.Map;

import static org.testng.Assert.*;

public class DecoratorTest {

    static class DummyService implements Service {
        @Override
        public Response execute(Request request) {
            return new Response(200, Map.of(), "original");
        }
    }

    @Test
    public void testLoggingDecorator_DoesNotChangeResponse() {
        Service dummy = new DummyService();
        LoggingDecorator decorator = new LoggingDecorator(dummy);
        Request req = new Request("/test", "GET", Map.of(), null, "1.1.1.1");
        Response resp = decorator.execute(req);
        assertEquals(resp.getBody(), "original");
    }

    @Test
    public void testCompressionDecorator_AddsPrefix() {
        Service dummy = new DummyService();
        CompressionDecorator decorator = new CompressionDecorator(dummy);
        Request req = new Request("/test", "GET", Map.of(), null, "1.1.1.1");
        Response resp = decorator.execute(req);
        assertTrue(resp.getBody().startsWith("[compressed]"));
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testRetryDecorator_RetriesOnFailure() {
        Service failing = request -> { throw new RuntimeException("Service down"); };
        RetryDecorator decorator = new RetryDecorator(failing, 3);
        Request req = new Request("/test", "GET", Map.of(), null, "1.1.1.1");
        decorator.execute(req);
    }
}