package test.com.gateway.filter;

import main.com.gateway.filter.AuthFilter;
import org.testng.annotations.Test;
import test.com.gateway.filter.testutil.TestFilterChain;
import main.com.gateway.model.Request;
import main.com.gateway.model.Response;

import java.util.Map;

import static org.testng.Assert.*;

public class AuthFilterTest {

    @Test
    public void testMissingToken_Returns401() {
        AuthFilter filter = new AuthFilter();
        Request req = new Request("/test", "GET", Map.of(), null, "127.0.0.1");
        TestFilterChain chain = new TestFilterChain();
        filter.handle(req, chain);
        Response resp = chain.getFinalResponse();
        assertNotNull(resp);
        assertEquals(resp.getStatusCode(), 401);
    }

    @Test
    public void testInvalidToken_Returns401() {
        AuthFilter filter = new AuthFilter();
        Request req = new Request("/test", "GET", Map.of("Authorization", "Basic token"), null, "127.0.0.1");
        TestFilterChain chain = new TestFilterChain();
        filter.handle(req, chain);
        assertEquals(chain.getFinalResponse().getStatusCode(), 401);
    }

    @Test
    public void testValidToken_Proceeds() {
        AuthFilter filter = new AuthFilter();
        Request req = new Request("/test", "GET", Map.of("Authorization", "Bearer valid"), null, "127.0.0.1");
        TestFilterChain chain = new TestFilterChain();
        filter.handle(req, chain);
        assertNull(chain.getFinalResponse()); // не заблокирован
    }
}