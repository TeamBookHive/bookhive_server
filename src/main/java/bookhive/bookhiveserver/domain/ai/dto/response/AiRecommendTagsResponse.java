package bookhive.bookhiveserver.domain.ai.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AiRecommendTagsResponse {

    @JsonProperty(required = true, value = "tags")
    List<String> tags;
}
