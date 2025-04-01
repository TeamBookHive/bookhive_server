package bookhive.bookhiveserver.testUtil;

import bookhive.bookhiveserver.domain.post.dto.PostRequest;
import bookhive.bookhiveserver.domain.tag.dto.request.TagRequest;
import java.util.List;

public class PostDtoMother {

    public static PostRequest createPostRequest(String content, List<TagRequest> tags, String processId) {
        PostRequest dto = new PostRequest();
        dto.setContent(content);
        dto.setTags(tags);
        dto.setProcessId(processId);

        return dto;
    }
}
