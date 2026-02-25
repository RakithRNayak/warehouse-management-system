package com.wms.gateway.config;

import com.wms.gateway.filter.AuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private final AuthenticationFilter authFilter;

    public GatewayConfig(AuthenticationFilter authFilter) {
        this.authFilter = authFilter;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Auth Service routes
                .route("auth-service", r -> r
                        .path("/api/auth/**")
                        .filters(f -> f.filter(authFilter.apply(
                                new AuthenticationFilter.Config(true))))
                        .uri("lb://auth-service"))
                .route("user-service", r -> r
                        .path("/api/users/**", "/api/roles/**")
                        .filters(f -> f.filter(authFilter.apply(
                                new AuthenticationFilter.Config(false))))
                        .uri("lb://auth-service"))

                // Inventory Service routes
                .route("inventory-service", r -> r
                        .path("/api/products/**", "/api/categories/**",
                                "/api/stock/**", "/api/stock-movements/**",
                                "/api/cycle-counts/**", "/api/reports/inventory/**")
                        .filters(f -> f.filter(authFilter.apply(
                                new AuthenticationFilter.Config(false))))
                        .uri("lb://inventory-service"))

                // Warehouse Service routes
                .route("warehouse-service", r -> r
                        .path("/api/warehouses/**", "/api/zones/**",
                                "/api/aisles/**", "/api/racks/**",
                                "/api/bins/**")
                        .filters(f -> f.filter(authFilter.apply(
                                new AuthenticationFilter.Config(false))))
                        .uri("lb://warehouse-service"))

                // Order Service routes
                .route("order-service", r -> r
                        .path("/api/orders/**", "/api/pick-lists/**",
                                "/api/packing/**", "/api/returns/**",
                                "/api/reports/orders/**")
                        .filters(f -> f.filter(authFilter.apply(
                                new AuthenticationFilter.Config(false))))
                        .uri("lb://order-service"))

                // Shipping Service routes
                .route("shipping-service", r -> r
                        .path("/api/carriers/**", "/api/shipments/**",
                                "/api/dispatch/**", "/api/reports/shipping/**")
                        .filters(f -> f.filter(authFilter.apply(
                                new AuthenticationFilter.Config(false))))
                        .uri("lb://shipping-service"))

                // Notification Service routes
                .route("notification-service", r -> r
                        .path("/api/notifications/**")
                        .filters(f -> f.filter(authFilter.apply(
                                new AuthenticationFilter.Config(false))))
                        .uri("lb://notification-service"))

                .build();
    }
}
