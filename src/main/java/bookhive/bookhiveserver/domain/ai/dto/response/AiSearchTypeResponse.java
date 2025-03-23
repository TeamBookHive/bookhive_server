package bookhive.bookhiveserver.domain.ai.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AiSearchTypeResponse {

    @JsonProperty(required = true, value = "isSearch")
    private String isSearch;

    @JsonProperty(required = true, value = "keyword")
    private String keyword;
}