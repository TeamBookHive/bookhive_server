package bookhive.bookhiveserver.domain.post.dto;

import bookhive.bookhiveserver.domain.tag.entity.Tag;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class PostRequest {
    private String content;
    private List<Tag> tags = new ArrayList<>();
}