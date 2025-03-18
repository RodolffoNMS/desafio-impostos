package com.zup.desafio_imposto.config.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
public class AuthenticationEntryPointConfig {

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(createErrorResponse("Unauthorized"));
        };
    }

    private String createErrorResponse(String message) {
        return String.format("{\"error\": \"%s\"}", message);
    }
}