package bookhive.bookhiveserver.domain.ai.dto.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContentRequest {
    private String content;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private String processId;
}
