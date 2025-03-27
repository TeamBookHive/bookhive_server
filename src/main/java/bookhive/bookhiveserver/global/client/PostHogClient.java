package bookhive.bookhiveserver.global.client;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PostHogClient {

    private final WebClient webClient;

    @Value("${posthog.base-url}")
    private String url;

    @Value("${posthog.api-key}")
    private String apiKey;

    public Mono<Void> captureEvent(Long userId, String eventName, Map<String, Object> properties) {
        return webClient.post()
                .uri(url)
                .bodyValue(Map.of(
                        "api_key", apiKey,
                        "event", eventName,
                        "distinct_id", userId,
                        "properties", properties
                ))
                .retrieve()
                .bodyToMono(Void.class);
    }
}
