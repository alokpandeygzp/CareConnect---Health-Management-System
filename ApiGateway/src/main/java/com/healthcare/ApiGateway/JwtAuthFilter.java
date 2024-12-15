package com.healthcare.ApiGateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);


    public static class Config {
    }

    public JwtAuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (validator.isSecured.test(request)) {
                if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    logger.error("Missing Authorization header for request to {}", request.getURI());
                    throw new RuntimeException("Missing Authorization header");
                }

                String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    logger.error("Invalid Authorization header format for request to {}", request.getURI());
                    throw new RuntimeException("Invalid Authorization header format");
                }

                String token = authHeader.substring(7);  // Remove "Bearer "
                try {
                    jwtUtil.validateToken(token);
                } catch (Exception e) {
                    logger.error("Token validation failed for request to {}: {}", request.getURI(), e.getMessage());
                    throw new RuntimeException("Unauthorized access to the application");
                }
            }
            return chain.filter(exchange);
        };
    }
}