package bookhive.bookhiveserver.global.event.content;

import bookhive.bookhiveserver.global.client.PostHogClient;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContentFixedEventListener {

    private final PostHogClient postHogClient;

    @EventListener
    public void handleContentFixed(ContentFixedEvent event) {

        postHogClient.captureEvent(
                event.getUserId(),
                "Text_Scanned",
                Map.of(
                        "process_id", event.getProcessId(),
                        "text", event.getContent()
                )
        ).subscribe();
    }
}
