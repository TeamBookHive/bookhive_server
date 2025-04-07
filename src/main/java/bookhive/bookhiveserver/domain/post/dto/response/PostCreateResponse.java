package bookhive.bookhiveserver.domain.post.dto.response;

import bookhive.bookhiveserver.domain.book.dto.response.BookDetail;
import bookhive.bookhiveserver.domain.tag.dto.response.TagResponse;
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
    private List<TagResponse> tags;
    private BookDetail book;
}
