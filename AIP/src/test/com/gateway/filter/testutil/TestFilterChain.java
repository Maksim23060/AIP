package test.com.gateway.filter.testutil;

import main.com.gateway.filter.FilterChain;
import main.com.gateway.model.Response;
import main.com.gateway.model.Request;

import java.util.Collections;

public class TestFilterChain extends FilterChain {
    public TestFilterChain() {
        super(Collections.emptyList());
    }

    @Override
    public void proceed(Request request) {
    }

    public Response getFinalResponse() {
        return null;
    }
}