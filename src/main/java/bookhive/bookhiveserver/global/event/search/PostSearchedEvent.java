package bookhive.bookhiveserver.global.event.search;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostSearchedEvent {
    private Long userId;
    private String question;

    public static PostSearchedEvent create(Long userId, String question) {
        return PostSearchedEvent.builder()
                .userId(userId)
                .question(question)
                .build();
    }
}