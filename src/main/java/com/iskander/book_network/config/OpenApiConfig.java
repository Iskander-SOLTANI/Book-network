package com.iskander.book_network.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "iskander",
                        email = "sol.iskander.dev@gmail.com"
                ),
                description = "OpenApiDocumentation for spring boot security",
                title = "OpenApiSpecification - iskander",
                version = "1.0",
                license = @License(
                        name = "license name",
                        url = "https//some-url.com"
                ),
                termsOfService = "Terms of service"
        ),
        servers = {
                @Server(
                       description = "Local ENV",
                        url = "http://localhost:8088/api/v1"
                ),
                @Server(
                        description = " PROD ENV",
                        url = "https://skoncoding.com/courses"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuthentication"
                )
        }
)
@SecurityScheme(
        name = "bearerAuthentication",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
