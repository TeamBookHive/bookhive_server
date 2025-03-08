package bookhive.bookhiveserver.domain.clova.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchTypeResponse {
    private String isSearch;
    private String keyword;
}
