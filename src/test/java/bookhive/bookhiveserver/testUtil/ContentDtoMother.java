package bookhive.bookhiveserver.testUtil;

import bookhive.bookhiveserver.domain.ai.dto.request.AiCorrectTextRequest;
import bookhive.bookhiveserver.domain.ai.dto.request.AiRecommendTagsRequest;

public class ContentDtoMother {

    public static AiRecommendTagsRequest createRecommendTagRequest(String content) {
        AiRecommendTagsRequest dto = new AiRecommendTagsRequest();
        dto.setContent(content);

        return dto;
    }

    public static AiCorrectTextRequest createContentRequest(String content, String processId) {
        AiCorrectTextRequest dto = new AiCorrectTextRequest();
        dto.setContent(content);
        dto.setProcessId(processId);

        return dto;
    }
}
