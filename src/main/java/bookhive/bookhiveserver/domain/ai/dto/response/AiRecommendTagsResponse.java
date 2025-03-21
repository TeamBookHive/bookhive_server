package bookhive.bookhiveserver.domain.ai.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AiRecommendTagsResponse {
    List<String> tags;
}
