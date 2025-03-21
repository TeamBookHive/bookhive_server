package bookhive.bookhiveserver.domain.ai.client;

import bookhive.bookhiveserver.domain.ai.dto.response.AiKeywordsResponse;
import bookhive.bookhiveserver.domain.ai.dto.response.AiSearchTypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpenAiSearchClient implements AiSearchClient{

    private final ChatClient chatClient;

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

        return this.chatClient.prompt()
                .system(prompt)
                .user(content)
                .call()
                .entity(AiSearchTypeResponse.class);
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

        return this.chatClient.prompt()
                .system(prompt)
                .user(content)
                .call()
                .entity(AiKeywordsResponse.class);
    }
}
