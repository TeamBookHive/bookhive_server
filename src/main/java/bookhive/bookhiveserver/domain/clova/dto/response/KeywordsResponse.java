package bookhive.bookhiveserver.domain.clova.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeywordsResponse {
    private List<String> keywords;
}
