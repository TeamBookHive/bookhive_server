package bookhive.bookhiveserver.domain.book.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookShowResponse {
    private Long id;
    private String title;
    private String author;
    private String imageUrl;
    private LocalDateTime createdAt;
}
