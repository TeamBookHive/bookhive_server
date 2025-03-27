package bookhive.bookhiveserver.global.event.post;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostCreatedEvent {
    private Long userId;

    public static PostCreatedEvent create(Long userId) {
        return PostCreatedEvent.builder()
                .userId(userId)
                .build();
    }
}
