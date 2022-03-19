package com.team701.buddymatcher;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@SecurityScheme(
        name = "JWT",
        description = "JWT authentication with bearer token",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "Bearer [token]"
)
public class SwaggerConfig {

    @Bean
    public OpenAPI myAPI() {
        return new OpenAPI()
                .info(new Info().title("MyApp Rest APIs")
                        .description("APIs for MyApp.")
                        .version("1.0")
                        .termsOfService("Terms of service")
                        .contact(new Contact().name("test").url("www.org.com").email("test@emaildomain.com"))
                        .license(new License().name("License of API").url("API license URL")))
                .externalDocs(new ExternalDocumentation()
                        .description("My ApI Wiki Documentation")
                        .url("https://github.com/SE701-T1/backend/wiki"));

    }
}
