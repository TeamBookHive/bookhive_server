package bookhive.bookhiveserver.global.event.search;

import bookhive.bookhiveserver.global.client.PostHogClient;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostSearchedEventListener {

    private final PostHogClient postHogClient;

    @EventListener
    public void handlePostSearched(PostSearchedEvent event) {
        postHogClient.captureEvent(
                event.getUserId(),
                "Post_Searched",
                Map.of("question", event.getQuestion())
        ).subscribe();
    }
}
