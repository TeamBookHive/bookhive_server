package bookhive.bookhiveserver.testUtil;

import bookhive.bookhiveserver.domain.ai.dto.request.ContentRequest;
import bookhive.bookhiveserver.domain.ai.dto.request.RecommendTagRequest;

public class ContentDtoMother {

    public static RecommendTagRequest createRecommendTagRequest(String content) {
        RecommendTagRequest dto = new RecommendTagRequest();
        dto.setContent(content);

        return dto;
    }

    public static ContentRequest createContentRequest(String content, String processId) {
        ContentRequest dto = new ContentRequest();
        dto.setContent(content);
        dto.setProcessId(processId);

        return dto;
    }
}
