package main.com.gateway.filter;

import main.com.gateway.model.Request;
import main.com.gateway.model.Response;

import java.util.List;

public class FilterChain {
    private final List<Filter> filters;
    private int index = 0;
    private Response finalResponse;

    public FilterChain(List<Filter> filters) {
        this.filters = filters;
    }

    public void proceed(Request request) {
        if (finalResponse == null && index < filters.size()) {
            Filter filter = filters.get(index);
            index++;
            filter.handle(request, this);
        }
    }

    public void setFinalResponse(Response response) {
        this.finalResponse = response;
    }

    public Response getFinalResponse() {
        return finalResponse;
    }

    public void reset() {
        this.index = 0;
        this.finalResponse = null;
    }

}