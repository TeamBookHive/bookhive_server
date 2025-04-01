package bookhive.bookhiveserver.domain.book.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookShowLatestResponse {

    private Long id;
    private String title;
    private String author;
    private LocalDateTime createdAt;

    public static BookShowLatestResponse empty() {
        return new BookShowLatestResponse(null, null, null, null);
    }
}
