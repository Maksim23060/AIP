package test.com.gateway;

import main.com.gateway.Gateway;
import main.com.gateway.config.ConfigLoader;
import main.com.gateway.model.Request;
import main.com.gateway.model.Response;
import main.com.gateway.model.ServiceInstance;
import main.com.gateway.proxy.ServiceProxy;
import main.com.gateway.strategy.RoundRobinStrategy;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import static org.testng.Assert.*;

public class GatewayIntegrationTest {
    private Gateway gateway;

    @BeforeClass
    public void setUp() throws IOException {

        Path tempFile = Files.createTempFile("gateway_test", ".txt");
        String config = "svc1,http://localhost:8081,1,true\nsvc2,http://localhost:8082,2,true";
        Files.writeString(tempFile, config);
        List<ServiceInstance> services = ConfigLoader.loadServices(tempFile.toString());
        ServiceProxy proxy = new ServiceProxy();
        gateway = new Gateway(services, new RoundRobinStrategy(), proxy);
        Files.delete(tempFile);
    }

    @Test
    public void testFullRequestFlow_ReturnsSuccess() {
        Request req = new Request("/test", "GET", Map.of("Authorization", "Bearer abc"), null, "127.0.0.1");
        Response resp = gateway.handleRequest(req);
        assertEquals(resp.getStatusCode(), 200);
        assertTrue(resp.getBody().contains("result"));
    }

    @Test
    public void testUnauthorizedRequest_Returns401() {
        Request req = new Request("/test", "GET", Map.of(), null, "127.0.0.1");
        Response resp = gateway.handleRequest(req);
        assertEquals(resp.getStatusCode(), 401);
    }
}