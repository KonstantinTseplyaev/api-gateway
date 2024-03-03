package com.aston.hw4.apigateway.filter;

import com.aston.hw4.apigateway.util.JwtExtractor;
import com.aston.hw4.apigateway.util.RouteValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private RouteValidator routeValidator;
    @Autowired
    private JwtExtractor jwtExtractor;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (routeValidator.isSecured.test(exchange.getRequest())) {

                HttpHeaders headers = exchange.getRequest().getHeaders();

                if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("missing authorization header");
                }

                String authToken = Objects.requireNonNull(headers.get(HttpHeaders.AUTHORIZATION)).get(0);

                if (authToken != null && authToken.startsWith("Bearer ")) {
                    authToken = authToken.substring(7);

                    boolean isValidToken = Boolean.TRUE.equals(restTemplate
                            .getForObject("http://localhost:8091/auth/valid?token=" + authToken,
                                    Boolean.class));

                    if (!isValidToken) {
                        throw new RuntimeException("token is not valid!");
                    } else {
                        String userId = String.valueOf(jwtExtractor.extractUserId(authToken));
                        HttpHeaders writableHeaders = HttpHeaders.writableHttpHeaders(headers);
                        writableHeaders.add("userId", userId);
                    }
                }
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {

    }
}
