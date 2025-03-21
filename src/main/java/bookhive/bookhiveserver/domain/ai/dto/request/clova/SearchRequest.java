package bookhive.bookhiveserver.domain.ai.dto.request.clova;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {
    private String question;
}
