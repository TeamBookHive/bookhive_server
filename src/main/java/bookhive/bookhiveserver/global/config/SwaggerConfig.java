package bookhive.bookhiveserver.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@OpenAPIDefinition(
    servers = {
            @Server(url = "https://bookchive.com", description = "production"),
            @Server(url = "http://localhost:8080", description = "develop")
    })
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("Authorization"))
                .components(new Components()
                        .addSecuritySchemes("Authorization",
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)))
                .info(new Info().title("BookHive API").version("1.0").description("BookHive 백엔드 API 명세서"));
    }
}