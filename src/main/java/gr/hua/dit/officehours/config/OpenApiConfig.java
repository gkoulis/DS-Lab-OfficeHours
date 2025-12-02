package gr.hua.dit.officehours.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;

import io.swagger.v3.oas.models.info.Info;

import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI (Swagger) configuration.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Office Hours API")
                .version("v1")
                .description("Stateless API for data analytics purposes")
            )
            .components(new Components()
                .addSecuritySchemes("BearerAuth", new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                )
            )
            .addSecurityItem(new SecurityRequirement().addList("BearerAuth"));
    }

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
            .group("api")
            .packagesToScan("gr.hua.dit.officehours.web.rest")
            .pathsToMatch("/api/v1/**")
            .build();
    }
}
