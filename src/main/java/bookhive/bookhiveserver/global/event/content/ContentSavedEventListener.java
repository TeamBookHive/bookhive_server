package bookhive.bookhiveserver.global.event.content;

import bookhive.bookhiveserver.global.client.PostHogClient;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContentSavedEventListener {

    private final PostHogClient postHogClient;

    @EventListener
    public void handleContentSaved(ContentSavedEvent event) {

        postHogClient.captureEvent(
                event.getUserId(),
                "Text_Saved",
                Map.of(
                        "process_id", event.getProcessId(),
                        "text", event.getContent()
                )
        ).subscribe();
    }
}
