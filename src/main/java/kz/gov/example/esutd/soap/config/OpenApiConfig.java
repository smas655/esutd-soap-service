package kz.gov.example.esutd.soap.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI esutdOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ESUTD SOAP Service API")
                        .description("Сервис взаимодействия с ЕСУТД через ШЭП")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("ESUTD Support")
                                .email("support@example.gov.kz"))
                        .license(new License()
                                .name("Private")
                                .url("https://example.gov.kz/licenses")))
                .servers(List.of(
                        new Server()
                                .url("/")
                                .description("Сервер по умолчанию")
                ));
    }
} 