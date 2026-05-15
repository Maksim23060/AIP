package main.com.gateway.model;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class ServiceInstance {
    private final String id;
    private final String url;
    private final int weight;
    private volatile boolean alive;
    private final AtomicInteger activeConnections;

    public ServiceInstance(String id, String url, int weight, boolean alive) {
        this.id = id;
        this.url = url;
        this.weight = weight;
        this.alive = alive;
        this.activeConnections = new AtomicInteger(0);
    }

    public String getId() { return id; }
    public String getUrl() { return url; }
    public int getWeight() { return weight; }
    public boolean isAlive() { return alive; }
    public void setAlive(boolean alive) { this.alive = alive; }
    public int getActiveConnections() { return activeConnections.get(); }
    public void incrementConnections() { activeConnections.incrementAndGet(); }
    public void decrementConnections() { activeConnections.decrementAndGet(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceInstance that = (ServiceInstance) o;
        return weight == that.weight &&
                alive == that.alive &&
                Objects.equals(id, that.id) &&
                Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, weight, alive);
    }

    @Override
    public String toString() {
        return "ServiceInstance{id='" + id + "', url='" + url + "', weight=" + weight + ", alive=" + alive + "}";
    }
}