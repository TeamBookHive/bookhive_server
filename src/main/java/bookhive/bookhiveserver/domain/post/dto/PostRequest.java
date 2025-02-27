package bookhive.bookhiveserver.domain.post.dto;

import bookhive.bookhiveserver.domain.tag.dto.TagRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class PostRequest {
    private String content;
    private List<TagRequest> tags = new ArrayList<>();
}