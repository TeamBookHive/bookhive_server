package bookhive.bookhiveserver.domain.clova.client;

import bookhive.bookhiveserver.domain.clova.dto.request.ClovaMessage;
import bookhive.bookhiveserver.domain.clova.dto.request.ClovaRequest;
import bookhive.bookhiveserver.domain.clova.dto.response.ClovaResponse;
import bookhive.bookhiveserver.domain.clova.dto.response.KeywordsResponse;
import bookhive.bookhiveserver.domain.clova.dto.response.SearchTypeResponse;
import bookhive.bookhiveserver.global.exception.ErrorMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

@Component
@Slf4j
@RequiredArgsConstructor
public class ClovaSearchApiClient {
    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${clova.search-url}")
    private String clovaUrl;

    @Value("${clova.api-key}")
    private String clovaApiKey;

    public SearchTypeResponse checkSearchType(String question) {

        List<ClovaMessage> messages = List.of(
                new ClovaMessage("system",
                        """
                                - 사용자의 질문을 분석하여, 검색어가 명확한지 판단해주세요.

                                # 예시

                                가을이 들어간 구절을 검색해줘.
                                { "isSearch": "true", "keyword": "가을" }
                                ###
                                마케팅이란 문구가 들어간 기록을 보고싶어.
                                { "isSearch": "true", "keyword": "마케팅" }
                                ###
                                마케팅에 도움이 되는 기록을 보여줘.
                                { "isSearch": "false", "keyword": null }
                                ###
                                공부라는 키워드가 들어간 아카이브를 보여줘.
                                { "isSearch": "true", "keyword": "공부" }
                                ###
                                공부에 집중할 수 있도록 마음을 다잡는 데 도움을 주는 구절을 보여줘.
                                { "isSearch": "false", "keyword": null }
                                ###
                                인간관계에서 도움을 얻을 수 있는 구절이 있어?
                                { "isSearch": "false", "keyword": null }
                                ###
                                자꾸 불안함이 들 때 위로가 되는 문구를 보고 싶어.
                                { "isSearch": "false", "keyword": null }
                                ###
                                운동
                                { "isSearch": "true", "keyword": "운동" }
                                ###
                                배고파
                                { "isSearch": "false", "keyword": null }
                                ###
                                기분이 좋지 않아.
                                { "isSearch": "false", "keyword": null }

                                - 다음 질문을 보고 같은 형식으로 JSON을 반환하세요."""),
                new ClovaMessage("user", question)
        );

        ClovaRequest request = new ClovaRequest(messages);

        String jsonString = webClient.post()
                            .uri(clovaUrl)
                            .header("Authorization", clovaApiKey)
                            .header("Content-Type", "application/json")
                            .bodyValue(request)
                            .retrieve()
                            .bodyToMono(ClovaResponse.class)
                            .map(clovaResponse -> clovaResponse.getResult().getMessage().getContent())
                            .block();

        System.out.println("키워드 검색 결과: \n" + jsonString);

        try {
            return objectMapper.readValue(jsonString, SearchTypeResponse.class);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ErrorMessage.JSON_PARSE_ERROR.toString(), e);
        }
}

    public KeywordsResponse extractKeywords(String question, String originTags) {

        List<ClovaMessage> messages = List.of(
                new ClovaMessage("system",
                        """
                                - 사용자의 태그 리스트에서 질문과 관련 있는 키워드를 추출하세요.
                                - 질문과 관련된 새로운 키워드도 추출하세요.
                                - 키워드는 최대 8개까지 추출하세요.
                                - 답변 형식은 JSON 형식으로 답변해주세요.

                                # 예시

                                질문: 인간관계에 도움이 되는 구절이 있을까?
                                태그 리스트: ["철학", "심리", "사랑", "우정"]

                                { "keywords": ["심리", "사랑", "우정", "인간관계", "소통", "대화", "친구", "감정"] }
                                ###
                                질문: 취미활동에 도움이 되는 아카이브를 보여줘.
                                태그 리스트: ["개발", "공부", "일상", "스포츠", "여행", "마케팅"]

                                { "keywords": ["일상", "스포츠", "여행", "취미", "즐거움"] }
                                ###
                                질문: 자꾸 불안함이 들 때 위로가 되는 문구를 보고 싶어.
                                태그 리스트: ["마케팅", "세일즈", "우정", "마음가짐", "사랑", "공부"]

                                { "keywords": ["우정", "마음가짐", "사랑", "위로", "감정", "불안"] }
                                ###
                                질문: 지식 쌓기에 도움이 되는 문장들을 보여줘.
                                태그 리스트: ["개발", "공부", "디자인", "건강", "요리", "마음가짐"]

                                { "keywords": ["개발", "공부", "학습", "배움", "지식"] }
                                ###
                                질문: 중요한 시험을 앞뒀을 때 도움이 되는 구절을 추천해줘.
                                태그 리스트: ["인문학", "요리", "연인", "건강", "스포츠", "마음가짐"]

                                { "keywords": ["마음가짐", "긴장", "휴식", "자신감", "시험"] }

                                - 다음 질문과 사용자가 가진 태그 리스트를 보고, 관련된 키워드를 추출하세요.
                                """
                ),
                new ClovaMessage("user",
                        "질문: " + question + "\n"
                                + "태그 리스트: [" + originTags + "]\n"
                )
        );

        ClovaRequest request = new ClovaRequest(messages);

        String jsonString =  webClient.post()
                            .uri(clovaUrl)
                            .header("Authorization", clovaApiKey)
                            .header("Content-Type", "application/json")
                            .bodyValue(request)
                            .retrieve()
                            .bodyToMono(ClovaResponse.class)
                            .map(clovaResponse -> clovaResponse.getResult().getMessage().getContent())
                            .block();

        System.out.println("AI 검색 결과: \n" + jsonString);

        try {
            return objectMapper.readValue(jsonString, KeywordsResponse.class);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ErrorMessage.JSON_PARSE_ERROR.toString(), e);
        }
    }
}
