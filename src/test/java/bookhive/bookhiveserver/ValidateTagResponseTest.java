package bookhive.bookhiveserver;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import bookhive.bookhiveserver.domain.clova.service.ContentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(ContentService.class)
public class ValidateTagResponseTest {
    @Autowired
    private ContentService contentService;

    @Test
    void 올바른_형식일때_참을_반환한다() {
        boolean result = contentService.validateTagResponse("사랑, 우정, 용기");
        boolean result2 = contentService.validateTagResponse("고양이, 토끼, 고슴도치, 사슴, 강아지");

        assertThat(result).isTrue();
        assertThat(result2).isTrue();
    }

    @Test
    void 부연설명을_먼저_포함할_때_거짓을_반환한다() {
        boolean result = contentService.validateTagResponse("죄송합니다. 올바른 태그를 찾을 수 없습니다. 사랑, 우정, 용기");

        assertThat(result).isFalse();
    }

    @Test
    void 부연설명을_후에_포함할_때_거짓을_반환한다() {
        boolean result = contentService.validateTagResponse("사랑, 우정, 용기가 올바른 태그라고 생각합니다.");

        assertThat(result).isFalse();
    }

    @Test
    void 전혀_관련없는_응답은_거짓을_반환한다() {
        boolean result = contentService.validateTagResponse("고양이는 귀엽습니다 참말로.");

        assertThat(result).isFalse();
    }

    @Test
    void 개행문자가_포함될_때_거짓을_반환한다() {
        boolean result = contentService.validateTagResponse("사랑, 우정, 용기가 올바른 태그라고 생각합니다.\n 죄송합니다, 올바른 응답이 아닙니다.");

        assertThat(result).isFalse();
    }

    @Test
    void 공백을_포함한_태그도_사용할_수_있다() {
        boolean result = contentService.validateTagResponse("인간 관계, 데이터 사이언스, 고양이");

        assertThat(result).isTrue();
    }

    @Test
    void 공백_포함_시_발생할_수_있는_오류가_있다() {
        boolean result = contentService.validateTagResponse("인간 관계, 데이터 사이언스, 고양이입니다");

        assertThat(result).isTrue();
    }

    @Test
    void 공백을_두_번_이상_포함한_태그도_사용할_수_있다() {
        boolean result = contentService.validateTagResponse("템플릿 메서드 패턴, 데이터 사이언스");

        assertThat(result).isTrue();
    }

}
