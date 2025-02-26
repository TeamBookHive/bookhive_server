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
                        "너는 지금부터 OCR 인식을 통해 입력된 데이터를 맞춤법, 문법에 맞게 교정하는 작업을 완벽하게 수행하는 사람이야.\n" +
                                "입력 데이터에서 오타와 맞춤법을 교정해줘.\n" +
                                "단, 입력된 단어의 의미, 자모음, 문맥, 입력된 다른 단어와의 연관성을 기준으로 교정해줘.\n" +
                                "단어 수정의 경우 최대한 입력 데이터를 유지하는 선에서 교정해줘.\n" +
                                "띄어쓰기는 교정하되 줄바꿈 형태는 변경하면 안 돼.\n" +
                                "오직 교정된 문장만 출력하고 추가적인 설명, 대답은 포함하지 말아줘."
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
                        "너는 지금 OCR 기술로 인식된 책 구절들 데이터를 바탕으로, 이와 어울리는 태그를 추천해 줘야 해.\n" +
                                "기존에 존재하는 태그들이 주어질 거야. 기존에 존재하는 태그들로는 " + originTags + "가 있어.\n" +
                                "\n" +
                                "첫 번째로, 기존 태그들과 중복되지 않는 새로운 태그 3개를 반드시 추천해야 해. \n" +
                                "새로운 태그의 개수가 3개 미만이거나, 3개를 초과해서는 안 돼.\n" +
                                "\n" +
                                "두 번째로, 기존 태그들 중 어울리는 태그가 있다면 이 중에서 최대 2개를 선택해. \n" +
                                "어울리는 태그가 없다면 선택하지 않아도 돼.\n" +
                                "\n" +
                                "대답 형식은 반드시 \"태그명,태그명,태그명,태그명\"이어야 해. \n" +
                                "절대로 형식에서 벗어난 부연 설명, 대답, 추가적인 텍스트를 포함하지 마."

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
