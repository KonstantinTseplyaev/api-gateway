package com.aston.hw4.apigateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtExtractor {
    @Value("${jwt.secret}")
    private String secret;

    public Integer extractUserId(String jwtToken) {
        Claims claims = extractAllClaims(jwtToken);
        return (Integer) claims.get("id");
    }

    public String extractUserRole(String jwtToken) {
        Claims claims = extractAllClaims(jwtToken);
        return (String) claims.get("role");
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
}
