package com.microgestion.example.patterns.resilience.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Customer Management API", version = "1.0", description = "REST API for customer management with rate limiting capabilities", contact = @Contact(name = "Diego E. Mendoza", email = "diego_mendoza@microgestion.com", url = "https://www.linkedin.com/in/diegomendoza/"), license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html")), servers = {
                @Server(url = "/", description = "Default Server URL"),
                @Server(url = "http://localhost:8080", description = "Local development")
})
public class OpenApiConfig {

        @Bean
        OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .components(new Components()
                                                .addSecuritySchemes("bearer-key",
                                                                new SecurityScheme()
                                                                                .type(SecurityScheme.Type.HTTP)
                                                                                .scheme("bearer")
                                                                                .bearerFormat("JWT")));
        }
}