package bookhive.bookhiveserver.domain.ai.dto.response;

import lombok.Getter;

@Getter
public class CorrectTextResponse {
    private String content;

    public CorrectTextResponse(String content) {
        this.content = content;
    }
}
