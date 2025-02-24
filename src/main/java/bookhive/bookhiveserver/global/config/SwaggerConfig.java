package bookhive.bookhiveserver.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        Server server = new Server();
        server.setUrl("/");

        Info info = new Info()
                .title("BookHive API")
                .version("v1.0.0")
                .description("BookHive 백엔드 API 명세서");

        return new OpenAPI()
                .info(info)
                .servers(List.of(server));

    }
}
