package bookhive.bookhiveserver.domain.ai.dto.response.clova;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClovaKeywordsResponse {
    private List<String> keywords;
}
