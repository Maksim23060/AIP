package test.com.gateway.filter;


import main.com.gateway.filter.CacheFilter;
import org.mockito.Mockito;
import org.testng.annotations.Test;
import test.com.gateway.filter.testutil.TestFilterChain;
import main.com.gateway.model.Request;
import main.com.gateway.model.Response;
import main.com.gateway.proxy.ServiceProxy;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class CacheFilterTest {

    @Test
    public void testCacheHit_UsesCachedResponse() {
        ServiceProxy proxyMock = Mockito.mock(ServiceProxy.class);
        Request req = new Request("/test", "GET", Map.of(), null, "1.1.1.1");
        Response cachedResp = new Response(200, Map.of(), "cached");
        when(proxyMock.getFromCache(req)).thenReturn(cachedResp);

        CacheFilter filter = new CacheFilter(proxyMock);
        TestFilterChain chain = new TestFilterChain();
        filter.handle(req, chain);

        Response finalResp = chain.getFinalResponse();
        assertNotNull(finalResp);
        assertEquals(finalResp.getBody(), "cached");
        verify(proxyMock, never()).execute(any()); // не вызываем реальный сервис
    }

    @Test
    public void testCacheMiss_Proceeds() {
        ServiceProxy proxyMock = Mockito.mock(ServiceProxy.class);
        Request req = new Request("/test", "GET", Map.of(), null, "1.1.1.1");
        when(proxyMock.getFromCache(req)).thenReturn(null);

        CacheFilter filter = new CacheFilter(proxyMock);
        TestFilterChain chain = new TestFilterChain();
        filter.handle(req, chain);

        assertNull(chain.getFinalResponse()); // продолжить цепочку
    }
}