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

    @Value("${clova.request-id}")
    private String clovaRequestId;

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

        return webClient.post()
                .uri(clovaUrl)
                .header("Authorization", clovaApiKey)
                .header("X-NCP-CLOVASTUDIO-REQUEST-ID", clovaRequestId)
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ClovaResponse.class)
                .map(clovaResponse -> clovaResponse.getResult().getMessage().getContent())
                .doOnError(e -> log.error("Clova Studio API 호출에 실패했습니다.", e.getMessage()))
                .block();
    }
}
