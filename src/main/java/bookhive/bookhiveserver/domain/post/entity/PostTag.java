package bookhive.bookhiveserver.domain.post.entity;

import bookhive.bookhiveserver.domain.tag.entity.Tag;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@IdClass(PostTagId.class)
public class PostTag {

    @Id
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Id
    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    public static PostTag create(Post post, Tag tag) {
        PostTag postTag = PostTag.builder()
                        .post(post)
                        .tag(tag)
                        .build();
        post.getPostTags().add(postTag);
        tag.getPostTags().add(postTag);
        return postTag;
    }
}
