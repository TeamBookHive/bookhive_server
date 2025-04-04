package bookhive.bookhiveserver.domain.clova;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import bookhive.bookhiveserver.domain.ai.dto.request.ContentRequest;
import bookhive.bookhiveserver.domain.ai.service.content.ContentService;
import bookhive.bookhiveserver.domain.tag.entity.Tag;
import bookhive.bookhiveserver.domain.tag.repository.TagRepository;
import bookhive.bookhiveserver.domain.user.entity.User;
import bookhive.bookhiveserver.domain.user.repository.UserRepository;
import bookhive.bookhiveserver.testUtil.ContentDtoMother;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
public class ContentServiceIntegrationTest {

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private TagRepository tagRepository;

    @Autowired
    private ContentService contentService;

    User testUser;

    @BeforeEach
    void init() {
        testUser = User.create("testDeiceId", "testToken");
        when(userRepository.findByToken("testToken")).thenReturn(Optional.of(testUser));

        List<Tag> mockTags = List.of(
                Tag.create("자기계발", testUser),
                Tag.create("과학", testUser),
                Tag.create("경제", testUser)
        );
        when(tagRepository.findAllByUser(testUser)).thenReturn(mockTags);
    }

    @Test
    void 태그_추천_정상_추천_태그_리스트를_반환한다() {
        // given
        String token = testUser.getToken();

        ContentRequest request = ContentDtoMother.createContentRequest("""
                매일 짧은 시간만이라도 세련되고 아름다운 것.
                고상한 품격이나 운치를 가진 것을 찾아 마주하고 내 마음 밭에 어떤 감정 꽃이 피어나는지 구경하자.
                감정이 달팽이처럼 길게 늘여 뻗어 어디로 나의 갈 길을 알려주는지 관찰하자.""", "test");

        // when
        String result = contentService.recommendTags(request, token);

        // then
        assertThat(result).contains(",");
        System.out.println("태그 추천 응답 결과: " + result);
    }

}
