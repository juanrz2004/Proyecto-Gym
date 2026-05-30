package com.gym.gateway.filter;

import com.gym.gateway.config.GatewayConfig;
import com.gym.gateway.config.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

    private final JwtUtil jwtUtil;
    private final GatewayConfig gatewayConfig;

    public JwtAuthFilter(JwtUtil jwtUtil, GatewayConfig gatewayConfig) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
        this.gatewayConfig = gatewayConfig;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();

            // Rutas públicas: dejar pasar sin JWT
            boolean isPublic = gatewayConfig.getPublicPaths().stream()
                    .anyMatch(path::startsWith);
            if (isPublic) {
                return chain.filter(exchange);
            }

            // Verificar header Authorization
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return unauthorizedResponse(exchange, "Token JWT requerido");
            }

            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return unauthorizedResponse(exchange, "Formato de token inválido. Usa: Bearer <token>");
            }

            String token = authHeader.substring(7);

            if (!jwtUtil.isTokenValid(token)) {
                return unauthorizedResponse(exchange, "Token JWT inválido o expirado");
            }

            // Propagar username y role al microservicio destino
            String username = jwtUtil.extractUsername(token);
            String role = jwtUtil.extractRole(token);

            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-Auth-Username", username)
                    .header("X-Auth-Role", role)
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        };
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json");
        byte[] bytes = ("{\"error\":\"" + message + "\",\"status\":401}").getBytes();
        var buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }

    public static class Config {
        // Configuración futura por ruta (roles, etc.)
    }
}
