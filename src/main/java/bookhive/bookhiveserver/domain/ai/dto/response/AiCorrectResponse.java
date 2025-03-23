package bookhive.bookhiveserver.domain.ai.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AiCorrectResponse {

    @JsonProperty(required = true, value = "correctedContent")
    String correctedContent;
}
