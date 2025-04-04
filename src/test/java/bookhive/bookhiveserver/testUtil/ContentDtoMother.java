package bookhive.bookhiveserver.testUtil;

import bookhive.bookhiveserver.domain.ai.dto.request.ContentRequest;

public class ContentDtoMother {

    public static ContentRequest createContentRequest(String content, String processId) {
        ContentRequest dto = new ContentRequest();
        dto.setContent(content);
        dto.setProcessId(processId);

        return dto;
    }
}
