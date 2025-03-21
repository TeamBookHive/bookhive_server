package bookhive.bookhiveserver.domain.ai.dto.response;

import lombok.Getter;

@Getter
public class CorrectErrorsResponse {
    private String content;

    public CorrectErrorsResponse(String content) {
        this.content = content;
    }
}
