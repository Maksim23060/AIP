package test.com.gateway.strategy;

import main.com.gateway.strategy.LeastConnectionsStrategy;
import main.com.gateway.strategy.RoundRobinStrategy;
import main.com.gateway.strategy.WeightedStrategy;
import org.testng.annotations.Test;
import main.com.gateway.model.ServiceInstance;

import java.util.List;

import static org.testng.Assert.assertNotEquals;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class RoutingStrategyTest {

    @Test
    public void testRoundRobin_ReturnsInstancesCyclically() {
        List<ServiceInstance> services = List.of(
                new ServiceInstance("a", "http://a", 1, true),
                new ServiceInstance("b", "http://b", 1, true)
        );
        RoundRobinStrategy strategy = new RoundRobinStrategy();
        ServiceInstance first = strategy.selectService(services);
        ServiceInstance second = strategy.selectService(services);
        assertNotEquals(first, second);
    }

    @Test
    public void testRoundRobin_WithDeadServices_Skips() {
        List<ServiceInstance> services = List.of(
                new ServiceInstance("a", "http://a", 1, false),
                new ServiceInstance("b", "http://b", 1, true)
        );
        RoundRobinStrategy strategy = new RoundRobinStrategy();
        ServiceInstance selected = strategy.selectService(services);
        assertEquals(selected.getId(), "b");
    }

    @Test
    public void testWeightedStrategy_DistributesByWeight() {
        List<ServiceInstance> services = List.of(
                new ServiceInstance("heavy", "http://heavy", 10, true),
                new ServiceInstance("light", "http://light", 1, true)
        );
        WeightedStrategy strategy = new WeightedStrategy();
        int heavyCount = 0;
        for (int i = 0; i < 110; i++) {
            if (strategy.selectService(services).getId().equals("heavy")) heavyCount++;
        }
        assertTrue(heavyCount > 80); // примерно 10/11 от 110
    }

    @Test
    public void testLeastConnectionsStrategy_SelectsLeastBusy() {
        ServiceInstance s1 = new ServiceInstance("s1", "http://s1", 1, true);
        ServiceInstance s2 = new ServiceInstance("s2", "http://s2", 1, true);
        s1.incrementConnections();
        s1.incrementConnections(); // s1 имеет 2 соединения
        s2.incrementConnections(); // s2 имеет 1

        List<ServiceInstance> services = List.of(s1, s2);
        LeastConnectionsStrategy strategy = new LeastConnectionsStrategy();
        ServiceInstance selected = strategy.selectService(services);
        assertEquals(selected, s2);
    }
}