package main.com.gateway.filter;

import main.com.gateway.model.Request;
import main.com.gateway.model.Response;

import java.util.Map;

public class AuthFilter implements Filter {
    @Override
    public void handle(Request request, FilterChain chain) {
        String token = request.getHeaders().get("Authorization");
        System.out.println("[AuthFilter] Token: " + token);
        if (token == null || !token.startsWith("Bearer ")) {
            System.out.println("[AuthFilter] ❌ Unauthorized – setting 401");
            chain.setFinalResponse(new Response(401, Map.of("Content-Type", "text/plain"), "Unauthorized"));
            return;
        }
        System.out.println("[AuthFilter] ✅ Authorized – proceeding");
        chain.proceed(request);
    }
}