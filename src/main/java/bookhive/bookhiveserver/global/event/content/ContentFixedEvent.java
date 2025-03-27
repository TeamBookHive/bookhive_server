package bookhive.bookhiveserver.global.event.content;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContentFixedEvent {
    private Long userId;
    private String processId;
    private String content;

    public static ContentFixedEvent create(Long userId, String processId, String content) {
        return ContentFixedEvent.builder()
                .userId(userId)
                .processId(processId)
                .content(content)
                .build();
    }
}
