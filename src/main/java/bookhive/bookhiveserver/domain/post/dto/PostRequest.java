package bookhive.bookhiveserver.domain.post.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class PostRequest {
    private String content;
    private List<String> tags = new ArrayList<>();
}