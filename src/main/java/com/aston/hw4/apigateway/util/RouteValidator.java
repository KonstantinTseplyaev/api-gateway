package com.aston.hw4.apigateway.util;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    public static final List<String> openApEndpoints =
            List.of("auth/registration",
                    "auth/login");
    public Predicate<ServerHttpRequest> isSecured =
            request ->
                    openApEndpoints
                            .stream()
                            .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
