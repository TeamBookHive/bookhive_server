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
                        "# 역할\n"
                                + "당신은 OCR 인식을 통해 입력된 문장들을 맞춤법과 문법에 맞게 교정하는 전문 편집원입니다.\n"
                                + "독자들이 이해하기 쉽도록, 문장을 자연스럽고 정확한 형태로 수정하세요.\n\n"
                                + "# 제약 사항\n"
                                + "- 명확한 오류(맞춤법, 문법, 오타 등)만 수정하고, 문제가 없는 부분은 그대로 유지하세요.\n"
                                + "- 띄어쓰기는 교정하되 줄바꿈(개행)은 원본 그대로 유지하세요.\n"
                                + "- 방언이나 문체적 특징(예: 비유볍, 특정 작가의 스타일, 지역적 표현 등)을 교정하지 마세요.\n"
                                + "- 입력 문장의 단어 의미, 문맥을 고려하여 자연스럽게 수정하되, 원래 문장의 의도를 바꾸지 마세요.\n"
                                + "- 출력 형식은 원본 문장과 동일한 개행(줄바꿈) 구조를 유지해야 합니다.\n"
                                + "- 출력 형식은 반드시 원본과 동일한 개수의 문장으로 이루어져야 합니다.\n\n"
                                + "# 출력 규칙\n"
                                + "1. 출력은 반드시 입력과 같은 개수의 문장으로 이루어져야 합니다. 입력이 3문장이라면, 출력도 반드시 3문장이어야 합니다.\n"
                                + "2. 출력 형식은 원본 문장과 동일한 개행(줄바꿈) 구조를 유지해야 합니다.\n"
                                + "3. 추가적인 설명, 의견, 답변을 포함하지 마세요.\n"
                                + "4. 출력 문장의 일부를 임의로 생략하지 마세요.\n"
                                + "5. 출력은 오직 교정된 문장만 포함해야 하며, 추가적인 텍스트(예: \"입력:\", \"출력:\")는 포함하지 마세요.\n\n"
                                + "**위의 출력 형식을 어길 경우, 불이익이 있을 수 있습니다.**\n\n"
                                + "# 예제 입력 및 출력\n\n"
                                + "입력: 경협을 나누고 일으 11L 질수 있어야 한다.\n"
                                + "출력: 경험을 나누고 일으킬 수 있어야 한다.\n\n"
                                + "입력: 나는 이번 방학 동안 여행을 가기로 계확햇다.\n"
                                + "출력: 나는 이번 방학 동안 여행을 가기로 계획했다.\n\n"
                                + "입력: 나이가 들어서 들러붙은 병마를 억지로 페어내지 않겠다고 말했다.\n"
                                + "출력: 나이가 들어서 들러붙은 병마를 억지로 떼어내지 않겠다고 말했다."

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
                        "당신은 OCR 기술로 인식된 책 문장 데이터를 바탕으로 어울리는 태그를 추천하는 AI입니다. 다음 제약 사항을 바탕으로 가장 적절한 태그들을 추천해 주세요.\n\n"
                                + "# 기존 태그 목록\n"
                                + "기존 태그:" + originTags + "\n\n"
                                + "# 제약 사항\n"
                                + "- 태그의 총 개수는 3개 이상 5개 이하여야 합니다. 최종 결과물은 반드시 새로운 태그 3개와 기존 태그 최소 0개 최대 2개로 구성되어야 합니다.\n"
                                + "- 새로운 태그: 기존 태그들과 중복되지 않는 새로운 태그 3개를 반드시 생성하여 추천해야 합니다. 새로운 태그의 개수는 정확히 3개이며, 2개 또는 4개가 될 수 없습니다.\n"
                                + "- 기존 태그: 기존 태그 목록 중 어울리는 태그를 0개 이상 2개 이하 선택할 수 있습니다. 어울리는 태그가 없다면 선택하지 않아도 됩니다.\n"
                                + "- 출력 형식은 반드시 \"태그명, 태그명, 태그명\"의 형식으로 출력해야 합니다. 절대로 형식에서 벗어난 부연 설명, 대답, 추가적인 텍스트, 특수 문자를 포함하지 마세요. 제약 사항을 어길 경우 불이익이 존재할 수 있습니다.\n\n"
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
