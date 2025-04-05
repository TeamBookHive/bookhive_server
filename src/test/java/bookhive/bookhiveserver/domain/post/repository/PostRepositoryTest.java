package bookhive.bookhiveserver.domain.post.repository;

import static org.assertj.core.api.Assertions.assertThat;

import bookhive.bookhiveserver.domain.post.entity.Post;
import bookhive.bookhiveserver.domain.post.entity.PostTag;
import bookhive.bookhiveserver.domain.tag.entity.Tag;
import bookhive.bookhiveserver.domain.tag.repository.TagRepository;
import bookhive.bookhiveserver.domain.user.entity.User;
import bookhive.bookhiveserver.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Test
    void 태그_값과_유저로_최신순_게시글_5개_조회() {
        // given
        User user = userRepository.save(User.create("deviceId", "token"));
        Tag tag = tagRepository.save(Tag.create("개발", user));

        for (int i = 1; i <= 10; i++) {
            Post post = Post.create("개발 게시글 " + i, new ArrayList<>(), null, user);
            List<PostTag> postTags = List.of(PostTag.create(post, tag));
            postRepository.save(post);
        }

        // when
        List<String> result = postRepository.findTop5ByUserAndTagValueOrderByCreatedAtDesc(
                user, "개발",  PageRequest.of(0, 5));

        // then
        assertThat(result).hasSize(5);
        assertThat(result.get(0)).isEqualTo("개발 게시글 10");
        assertThat(result.get(4)).isEqualTo("개발 게시글 6");
    }
}
