package com.example.api_gateway.configuration;

import com.example.api_gateway.dto.ApiResponse;
import com.example.api_gateway.service.IdentityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class AuthenticationFilter implements GlobalFilter, Ordered {
    IdentityService identityService;
    ObjectMapper objectMapper;
    // list url not authentication
    @NonFinal // chi dinh khong la final neu duoc khai bao final
            String[] publicEndpoints = {
            "/identity/auth/.*",
            "/identity/users/register"};

    @Value("${app.api-prefix}")
    @NonFinal
    private String apiPrefix;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Enter authentication filter...");
        log.info("URL: {}",exchange.getRequest().getURI().getPath());
        if (isPublicEndpoints(exchange.getRequest()))
            return chain.filter(exchange);
        // get token from authorization
        // { check header xem da co token hay chua}
        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (CollectionUtils.isEmpty(authHeader))
            return unauthenticated(exchange.getResponse());

        // lay token tu header loai bo di cum tu "Bearer"
        String token = authHeader.getFirst().replace("Bearer", "");
        log.info("Token: {}", token);

        // verify token
        return identityService.introspect(token).flatMap(introspectResponseApiResponse -> {
            if (introspectResponseApiResponse.getResult().isValid()) {
                return chain.filter(exchange);
            } else
                return unauthenticated(exchange.getResponse());
        }).onErrorResume(_ -> unauthenticated(exchange.getResponse()));

    }

    @Override
    public int getOrder() {
        return -1;
        // number -> min
    }

    // { check header xem da co token hay chua}
    Mono<Void> unauthenticated(ServerHttpResponse response) {
        ApiResponse<?> apiResponse = ApiResponse.builder().code(666).message("Unauthenticated").build();

        String body = null;
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return response.writeWith(
                Mono.just(response.bufferFactory().
                        wrap(body.getBytes())
                ));
    }

    // check endpoints
    private boolean isPublicEndpoints(ServerHttpRequest request) {
        return Arrays.stream(publicEndpoints).anyMatch(s -> request.getURI().getPath().matches(apiPrefix + s));
    }
}