package bookhive.bookhiveserver.domain.post.dto.response;

import bookhive.bookhiveserver.domain.book.dto.response.BookDetail;
import bookhive.bookhiveserver.domain.post.entity.Post;
import bookhive.bookhiveserver.domain.tag.dto.response.TagResponse;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class PostDetail {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private List<TagResponse> tags;

    public PostDetail(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.tags = Optional.ofNullable(post.getPostTags())
                .orElse(Collections.emptyList())
                .stream()
                .map(postTag -> new TagResponse(postTag.getTag()))
                .collect(Collectors.toList());
    }
}
