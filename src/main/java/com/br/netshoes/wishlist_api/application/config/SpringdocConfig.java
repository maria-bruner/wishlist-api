package com.br.netshoes.wishlist_api.application.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Wishlist API",
                version = "1.0",
                description = "API para gerenciamento de lista de produtos por usuário."
        ),
        servers = {
                @Server(url = "http://localhost:8080")
        }
)
public class SpringdocConfig {
}