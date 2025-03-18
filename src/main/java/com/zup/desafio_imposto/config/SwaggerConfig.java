package com.zup.desafio_imposto.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                .title("API de Impostos")
                .version("1.0.0")
                .description("Documentação da API para gerenciamento de impostos"))
                .addSecurityItem(new SecurityRequirement()
                .addList("bearerAuth")) // Aplica o esquema de segurança globalmente aos endpoints protegidos.
                .components(new io.swagger.v3.oas.models.Components()
                .addSecuritySchemes("bearerAuth", new SecurityScheme() //Define o esquema de autenticação como bearer com o formato JWT.
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")));
    }}

