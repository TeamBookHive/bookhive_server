package bookhive.bookhiveserver.domain.clova.client;

import bookhive.bookhiveserver.domain.clova.dto.ClovaMessage;
import bookhive.bookhiveserver.domain.clova.dto.ClovaRequest;
import bookhive.bookhiveserver.domain.clova.dto.ClovaResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
@RequiredArgsConstructor
public class ClovaApiClient {
    private final WebClient webClient;

    @Value("${clova.url}")
    private String clovaUrl;

    @Value("${clova.api-key}")
    private String clovaApiKey;

    public String callFix(String content) {

        // system 프롬프트 주입
        List<ClovaMessage> messages = List.of(
                new ClovaMessage("system",
                        "너는 지금부터 OCR 인식을 통해 입력된 데이터를 맞춤법, 문법에 맞게 교정하는 작업을 수행하는 편집원이야.\n" +
                                "아래의 제약사항과 출력 예시를 바탕으로 생성해줘.\n" +
                                "\n" +
                                "#제약사항\n" +
                                "- 수정될만한 이유가 있는 것만을 수정해, 이유가 없으면 수정하지 마.\n" +
                                "- 띄어쓰기는 교정하되 줄바꿈 형태는 변경하지 마.\n" +
                                "- 입력된 단어의 의미, 자모음, 입력된 다른 단어와의 연관성을 기준으로 교정해.\n" +
                                "\n" +
                                "#출력문\n" +
                                "[지시문과 제약사항을 참고해 오직 교정된 문장만 출력하고 추가적인 설명, 대답은 포함하지 마.]"

                ),
                new ClovaMessage("user", content)
        );

        ClovaRequest request = new ClovaRequest(messages);

        return callApiByWebClient(request);
    }

    public String callRecommend(String content, String originTags) {

        // system 프롬프트 주입
        List<ClovaMessage> messages = List.of(
                new ClovaMessage("system",
                        "당신은 OCR 기술로 인식된 책 문장 데이터를 바탕으로 어울리는 태그를 추천하는 AI입니다.\n\n"
                                + "# 기존 태그 목록\n"
                                + "기존 태그: 사랑, 인간관계, 동물, 개발, 공부, 인생, 운동\n\n"
                                + "# 제약 사항\n"
                                + "1. 기존 태그들과 중복되지 않는 새로운 태그 3개를 반드시 생성하여 추천해야 합니다. 새로운 태그 개수는 정확히 3개여야 합니다.\n"
                                + "2. 추가로, 기존 태그 중 어울리는 태그를 최대 2개 선택할 수 있습니다. 어울리는 태그가 없다면 선택하지 않아도 됩니다.\n"
                                + "3. 출력 형식은 반드시 \"태그명, 태그명, 태그명\"으로만 출력해야 합니다. 절대로 형식에서 벗어난 부연 설명, 대답, 추가적인 텍스트를 포함하지 마세요.\n\n"
                                + "# 예제 입력 및 출력\n\n"
                                + "입력: \"사랑과 우정을 다룬 소설 속 인물들이 서로를 이해해 가는 과정이 감동적이다.\"\n"
                                + "출력: \"우정, 감동, 성장, 사랑, 인간관계\"\n\n"
                                + "입력: \"개발자가 되는 과정에서 겪는 어려움과 성취감을 그린 책이다.\"\n"
                                + "출력: \"코딩, 프로젝트, 도전, 개발, 공부\"\n\n"
                                + "입력: \"야생 동물들의 생태와 환경 보호에 대한 내용을 다룬 책이다.\"\n"
                                + "출력: \"환경, 생태, 자연, 동물\""

                ),
                new ClovaMessage("user", content)
        );

        ClovaRequest request = new ClovaRequest(messages);

        return callApiByWebClient(request);
    }

    private String callApiByWebClient(ClovaRequest request) {
        return webClient.post()
                .uri(clovaUrl)
                .header("Authorization", clovaApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ClovaResponse.class)
                .map(clovaResponse -> clovaResponse.getResult().getMessage().getContent())
                .doOnError(e -> log.error("Clova Studio API 호출에 실패했습니다.", e.getMessage()))
                .block();
    }
}
