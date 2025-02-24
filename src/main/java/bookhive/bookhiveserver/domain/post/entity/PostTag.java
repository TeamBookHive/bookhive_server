package bookhive.bookhiveserver.domain.post.entity;

import bookhive.bookhiveserver.domain.tag.entity.Tag;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
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

    public PostTag() {}

    public PostTag(Post post, Tag tag) {
        this.post = post;
        this.tag = tag;
        post.getPostTags().add(this);  // 양방향 관계 설정
        tag.getPostTags().add(this);
    }
}
