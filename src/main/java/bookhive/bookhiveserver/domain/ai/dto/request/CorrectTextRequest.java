package bookhive.bookhiveserver.domain.ai.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CorrectTextRequest {
    private String content;
    private String processId = "";
}
