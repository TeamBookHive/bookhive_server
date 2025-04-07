package bookhive.bookhiveserver.domain.post.dto.mapper;

import bookhive.bookhiveserver.domain.book.dto.response.BookDetail;
import bookhive.bookhiveserver.domain.post.dto.response.PostCreateResponse;
import bookhive.bookhiveserver.domain.post.entity.Post;
import bookhive.bookhiveserver.domain.tag.dto.response.TagResponse;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public class PostDtoMapper {

    public static PostCreateResponse toPostCreateResponse(Post post) {
        return PostCreateResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .tags(Optional.ofNullable(post.getPostTags())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(postTag -> new TagResponse(postTag.getTag()))
                        .collect(Collectors.toList()))
                .book(Optional.ofNullable(post.getBook())
                        .map(BookDetail::new)
                        .orElse(null))
                .build();
    }
}
