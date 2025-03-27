package bookhive.bookhiveserver.global.event.post;

import bookhive.bookhiveserver.global.client.PostHogClient;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostCreatedEventListener {

    private final PostHogClient postHogClient;

    @EventListener
    public void handlePostCreated(PostCreatedEvent event) {

        postHogClient.captureEvent(
                event.getUserId(),
                "Post_Created",
                Map.of()
        ).subscribe();
    }
}
