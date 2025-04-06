package bookhive.bookhiveserver.testUtil;

import bookhive.bookhiveserver.domain.ai.dto.request.CorrectTextRequest;
import bookhive.bookhiveserver.domain.ai.dto.request.RecommendTagsRequest;

public class ContentDtoMother {

    public static RecommendTagsRequest createRecommendTagRequest(String content) {
        RecommendTagsRequest dto = new RecommendTagsRequest();
        dto.setContent(content);

        return dto;
    }

    public static CorrectTextRequest createContentRequest(String content, String processId) {
        CorrectTextRequest dto = new CorrectTextRequest();
        dto.setContent(content);
        dto.setProcessId(processId);

        return dto;
    }
}
