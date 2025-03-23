package bookhive.bookhiveserver.domain.ai.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AiCorrectResponse {

    @JsonProperty(required = true, value = "correctedContent")
    String correctedContent;
}
