package com.example.api_gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth", r -> r.path("/auth/**")
                        .uri("http://localhost:8090"))

                .route("flight", r -> r.path("/flight/**")
                        .filters(f -> f.filter(jwtFilter))
                        .uri("http://localhost:8085"))

                .route("booking", r -> r.path("/booking/**")
                        .filters(f -> f.filter(jwtFilter))
                        .uri("http://localhost:8080"))

                .route("profile", r -> r.path("/profile/**")
                        .filters(f -> f.filter(jwtFilter))
                        .uri("http://localhost:8090"))
                .build();

    }
}