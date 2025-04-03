package bookhive.bookhiveserver.domain.post.dto.response;

import bookhive.bookhiveserver.domain.book.dto.response.BookDetail;
import bookhive.bookhiveserver.domain.post.entity.Post;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PostDetail {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private List<String> tags;
    private BookDetail book;

    public PostDetail(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.tags = Optional.ofNullable(post.getPostTags())
                .orElse(Collections.emptyList())
                .stream()
                .map(postTag -> postTag.getTag().getValue())
                .collect(Collectors.toList());
        this.book = Optional.ofNullable(post.getBook())
                .map(BookDetail::new)
                .orElse(null);
    }
}
