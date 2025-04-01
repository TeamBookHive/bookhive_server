package bookhive.bookhiveserver.domain.post.service;

import static org.assertj.core.api.Assertions.assertThat;

import bookhive.bookhiveserver.domain.post.entity.Post;
import bookhive.bookhiveserver.domain.tag.dto.request.TagRequest;
import bookhive.bookhiveserver.domain.user.entity.User;
import bookhive.bookhiveserver.domain.user.repository.UserRepository;
import bookhive.bookhiveserver.testUtil.PostDtoMother;
import bookhive.bookhiveserver.testUtil.TagDtoMother;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    User testUser;

    @BeforeEach
    void setUp() {
        testUser = userRepository.save(User.create("testDeviceId", "testToken"));
    }

    @Test
    void 기록_생성에_성공하면_생성된_기록을_반환한다()  {
        // given
        String content = "기록할 내용입니다.";
        String processId = "1";

        TagRequest newTagRequest1 = TagDtoMother.createTagRequest(null, "태그1");
        TagRequest newTagRequest2 = TagDtoMother.createTagRequest(null, "태그2");
        List<TagRequest> tags = List.of(newTagRequest1, newTagRequest2);

        // when
        Post result = postService.createPost(PostDtoMother.createPostRequest(content, tags, processId), testUser.getToken());

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo(content);
        assertThat(result.getPostTags()).hasSize(2);
        assertThat(result.getPostTags())
                .extracting(pt -> pt.getTag().getValue())
                .containsExactlyInAnyOrder("태그1", "태그2");
    }
}