package org.example.projectmanagerapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI projectManagerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Project Manager API")
                        .description("API for managing projects, tasks, and users")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Project Manager Team")
                                .email("contact@projectmanager.com")));
    }
}