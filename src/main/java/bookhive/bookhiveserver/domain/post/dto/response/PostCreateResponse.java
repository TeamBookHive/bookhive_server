package bookhive.bookhiveserver.domain.post.dto.response;

import bookhive.bookhiveserver.domain.book.dto.response.BookDetail;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostCreateResponse {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private List<String> tags;
    private BookDetail book;
}
