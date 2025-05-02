package bookhive.bookhiveserver.domain.book.dto.response;

import bookhive.bookhiveserver.domain.post.dto.response.PostDetail;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookShowDetailResponse {
    BookDetail book;
    List<PostDetail> posts;
}
