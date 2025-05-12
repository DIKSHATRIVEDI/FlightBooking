package com.example.api_gateway.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JwtFilter implements GatewayFilter {
    private final String secretKey = "supersecretkeythatisverylong123456";

    private final Map<String, List<String>> roleAccessMap = Map.ofEntries(
            Map.entry("/booking/create", List.of("USER", "ADMIN")),
            Map.entry("/booking/delete/{id}", List.of("USER", "ADMIN")),
            Map.entry("/booking/get/{id}", List.of("USER", "ADMIN")),
            Map.entry("/booking/viewPassengers/{flightId}", List.of("USER", "ADMIN")),
            Map.entry("/booking/confirm/{bookingId}", List.of("USER", "ADMIN")),
            Map.entry("/booking/get/user/{userId}", List.of("USER", "ADMIN")),

            Map.entry("/flight/create", List.of("ADMIN")),
            Map.entry("/flight/delete/{id}", List.of("ADMIN")),
            Map.entry("/flight/update/{id}", List.of("ADMIN")),
            Map.entry("/flight/updateSeats/{id}", List.of("ADMIN")),

            Map.entry("/flight/search", List.of("USER", "ADMIN")),
            Map.entry("/flight/getAll", List.of("USER", "ADMIN")),
            Map.entry("/flight/get/{id}", List.of("USER", "ADMIN")),

            Map.entry("/profile/getAll", List.of("ADMIN")),
            Map.entry("/profile/role/{role}", List.of("ADMIN"))
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        System.out.println(" JwtFilter triggered for path: " + path);

        if (path.startsWith("/auth/")) {
            return chain.filter(exchange);
        }

        List<String> authHeaders = exchange.getRequest().getHeaders().get("Authorization");
        System.out.println("Auth Headers: " + authHeaders);

        if (authHeaders == null || authHeaders.isEmpty() || !authHeaders.get(0).startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeaders.get(0).substring(7); // Remove "Bearer " prefix
        System.out.println(" Auth Header: " + token);

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            if (claims.getExpiration().before(new Date())) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String role = claims.get("roles", String.class);
            String username = claims.getSubject();
            System.out.println(" Role: " + role + ", Username: " + username);

            for (Map.Entry<String, List<String>> entry : roleAccessMap.entrySet()) {
                if (path.contains(entry.getKey()) && !entry.getValue().contains(role)) {
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }
            }

            exchange.getRequest().mutate()
                    .header("X-Username", username)
                    .header("X-Role", role)
                    .build();

        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }
}