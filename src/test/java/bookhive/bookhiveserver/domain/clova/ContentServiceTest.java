package bookhive.bookhiveserver.domain.clova;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import bookhive.bookhiveserver.domain.ai.dto.response.RecommendTagResponse;
import bookhive.bookhiveserver.domain.ai.service.ContentService;
import bookhive.bookhiveserver.domain.user.entity.User;
import bookhive.bookhiveserver.domain.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ContentServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ContentService contentService;

    User testUser;

    @BeforeEach
    void login() {
        testUser = new User("testDeiceId", "testToken");

        when(userRepository.findByToken("testToken")).thenReturn(Optional.of(testUser));
    }

    @Test
    void 잘못된_응답_형식이라면_기본_추천_태그_리스트를_반환한다() {
        // given
        String token = testUser.getToken();

        // when
        List<RecommendTagResponse> result = contentService.createRecommendTagList("잘못된 응답 테스트 입력 값입니다.", token);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result).extracting("value").containsExactly("일상", "기타");
    }
}
