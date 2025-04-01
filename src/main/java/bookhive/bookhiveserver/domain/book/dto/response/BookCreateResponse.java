package bookhive.bookhiveserver.domain.book.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookCreateResponse {

    private String title;
    private String author;
    private LocalDateTime createdAt;
}
