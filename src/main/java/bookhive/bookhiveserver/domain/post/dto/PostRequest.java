package bookhive.bookhiveserver.domain.post.dto;

import bookhive.bookhiveserver.domain.tag.dto.request.TagRequest;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class PostRequest {
    private String content;
    private List<TagRequest> tags = new ArrayList<>();

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private String processId;
}