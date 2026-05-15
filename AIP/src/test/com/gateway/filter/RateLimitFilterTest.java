package test.com.gateway.filter;


import main.com.gateway.filter.RateLimitFilter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.com.gateway.filter.testutil.TestFilterChain;
import main.com.gateway.model.Request;

import java.util.Map;

import static org.testng.Assert.*;

public class RateLimitFilterTest {
    private RateLimitFilter filter;
    private TestFilterChain chain;

    @BeforeMethod
    public void setUp() {
        filter = new RateLimitFilter();
        chain = new TestFilterChain();
    }

    @Test
    public void testWithinLimit_Allows() {
        String ip = "192.168.1.1";
        for (int i = 0; i < 10; i++) {
            Request req = new Request("/test", "GET", Map.of(), null, ip);
            filter.handle(req, chain);
            assertNull(chain.getFinalResponse());
        }
    }

    @Test
    public void testExceedsLimit_Rejects() {
        String ip = "192.168.1.2";
        for (int i = 0; i < 10; i++) {
            Request req = new Request("/test", "GET", Map.of(), null, ip);
            filter.handle(req, chain);
        }
        Request req = new Request("/test", "GET", Map.of(), null, ip);
        filter.handle(req, chain);
        assertNotNull(chain.getFinalResponse());
        assertEquals(chain.getFinalResponse().getStatusCode(), 429);
    }
}