package bookhive.bookhiveserver.domain.ai.client;

import bookhive.bookhiveserver.domain.ai.dto.response.AiCorrectResponse;
import bookhive.bookhiveserver.domain.ai.dto.response.AiKeywordsResponse;
import bookhive.bookhiveserver.domain.ai.dto.response.AiRecommendTagsResponse;
import bookhive.bookhiveserver.domain.ai.dto.response.AiSearchTypeResponse;
import bookhive.bookhiveserver.global.exception.ErrorMessage;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.ai.openai.api.ResponseFormat.Type;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpenAiClient implements AiClient {

    private final ChatClient chatClient;

    @Override
    public AiCorrectResponse correct(String content) {

        String prompt = """
                # 역할
                당신은 OCR 인식을 통해 입력된 문장들을 맞춤법과 문법에 맞게 교정하는 전문 편집원입니다.
                독자들이 이해하기 쉽도록, 문장을 자연스럽고 정확한 형태로 수정하세요.

                # 제약 사항
                - 명확한 오류(맞춤법, 문법, 오타 등)만 수정하고, 문제가 없는 부분은 그대로 유지하세요.
                - 띄어쓰기는 교정하되 줄바꿈(개행)은 원본 그대로 유지하세요.
                - 방언이나 문체적 특징(예: 비유볍, 특정 작가의 스타일, 지역적 표현 등)을 교정하지 마세요.
                - 입력 문장의 단어 의미, 문맥을 고려하여 자연스럽게 수정하되, 원래 문장의 의도를 바꾸지 마세요.
                - 출력 형식은 원본 문장과 동일한 개행(줄바꿈) 구조를 유지해야 합니다.
                - 출력 형식은 반드시 원본과 동일한 개수의 문장으로 이루어져야 합니다.

                # 출력 규칙
                1. 출력은 반드시 입력과 같은 개수의 문장으로 이루어져야 합니다. 입력이 3문장이라면, 출력도 반드시 3문장이어야 합니다.
                2. 출력 형식은 원본 문장과 동일한 개행(줄바꿈) 구조를 유지해야 합니다.
                3. 추가적인 설명, 의견, 답변을 포함하지 마세요.
                4. 출력 문장의 일부를 임의로 생략하지 마세요.
                5. 출력은 오직 교정된 문장만 포함해야 하며, 추가적인 텍스트(예: "입력:", "출력:")는 포함하지 마세요.

                **위의 출력 형식을 어길 경우, 불이익이 있을 수 있습니다.**

                # 예제 입력 및 출력

                입력: 경협을 나누고 일으 11L 질수 있어야 한다.
                출력: 경험을 나누고 일으킬 수 있어야 한다.

                입력: 나는 이번 방학 동안 여행을 가기로 계확햇다.
                출력: 나는 이번 방학 동안 여행을 가기로 계획했다.

                입력: 나이가 들어서 들러붙은 병마를 억지로 페어내지 않겠다고 말했다.
                출력: 나이가 들어서 들러붙은 병마를 억지로 떼어내지 않겠다고 말했다.""";

        return callWithStructuredOutput(content, prompt, AiCorrectResponse.class);
    }

    @Override
    public AiRecommendTagsResponse recommendTags(String content, String originTags) {

        String prompt = """
        당신은 OCR 기술로 인식된 책 문장 데이터를 바탕으로 어울리는 태그를 추천하는 AI입니다. 다음 제약 사항을 바탕으로 가장 적절한 태그들을 추천해 주세요.

        # 기존 태그 목록
        기존 태그: %s

        # 제약 사항
        - 태그의 총 개수는 3개 이상 5개 이하여야 합니다. 최종 결과물은 반드시 새로운 태그 3개와 기존 태그 최소 0개 최대 2개로 구성되어야 합니다.
        - 새로운 태그: 기존 태그들과 중복되지 않는 새로운 태그 3개를 반드시 생성하여 추천해야 합니다. 새로운 태그의 개수는 정확히 3개이며, 2개 또는 4개가 될 수 없습니다.
        - 기존 태그: 기존 태그 목록 중 어울리는 태그를 0개 이상 2개 이하 선택할 수 있습니다. 어울리는 태그가 없다면 선택하지 않아도 됩니다.
        - 출력은 오직 태그 리스트만 반환해야 합니다. 절대로 형식에서 벗어난 부연 설명, 대답, 추가적인 텍스트, 특수 문자를 포함하지 마세요. 제약 사항을 어길 경우 불이익이 존재할 수 있습니다.
        """.formatted(originTags);

        return callWithStructuredOutput(content, prompt, AiRecommendTagsResponse.class);
    }

    @Override
    public AiSearchTypeResponse checkSearchType(String content) {

        String prompt = """
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

                                - 다음 질문을 보고 같은 형식으로 JSON을 반환하세요.""";

        return callWithStructuredOutput(content, prompt, AiSearchTypeResponse.class);
    }

    @Override
    public AiKeywordsResponse extractKeywords(String question, String originTags) {

        String prompt = """
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
                                """;

        String content = "질문: " + question + "\n"
                + "태그 리스트: [" + originTags + "]\n";

        return callWithStructuredOutput(content, prompt, AiKeywordsResponse.class);
    }

    private <T> T callWithStructuredOutput(String user, String system, Class<T> clazz) {

        BeanOutputConverter<T> outputConverter = new BeanOutputConverter<>(clazz);
        String jsonSchema = outputConverter.getJsonSchema();

        Prompt prompt = new Prompt(user,
                OpenAiChatOptions.builder()
                        .responseFormat(new ResponseFormat(Type.JSON_SCHEMA, jsonSchema))
                        .build());

        ChatResponse chatResponse = this.chatClient.prompt(prompt)
                .system(system)
                .call()
                .chatResponse();

        return outputConverter.convert(extractSafeContent(chatResponse));
    }

    private String extractSafeContent(ChatResponse response) {

        if (response == null) {
            throw new RuntimeException(ErrorMessage.OPEN_AI_SERVER_ERROR.toString());
        }

        AssistantMessage message = response.getResult().getOutput();
        Map<String, Object> meta = message.getMetadata();

        if (!Objects.toString(meta.get("refusal"), "").isBlank()) {
            throw new RuntimeException(ErrorMessage.OPEN_AI_REFUSAL.toString());
        }

        return message.getText();
    }
}
