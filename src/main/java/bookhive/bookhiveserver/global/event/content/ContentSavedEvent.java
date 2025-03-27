package bookhive.bookhiveserver.global.event.content;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContentSavedEvent {
    private Long userId;
    private String processId;
    private String content;

    public static ContentSavedEvent create(Long userId, String processId, String content) {
        return ContentSavedEvent.builder()
                .userId(userId)
                .processId(processId)
                .content(content)
                .build();
    }
}
