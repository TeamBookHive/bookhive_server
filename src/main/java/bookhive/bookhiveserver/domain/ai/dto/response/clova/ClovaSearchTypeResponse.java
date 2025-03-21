package bookhive.bookhiveserver.domain.ai.dto.response.clova;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClovaSearchTypeResponse {
    private String isSearch;
    private String keyword;
}
