package bookhive.bookhiveserver.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    // 현재 직접 사용하고 있지 않음, Spring AI를 통해 사용하고 있음

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }
}
