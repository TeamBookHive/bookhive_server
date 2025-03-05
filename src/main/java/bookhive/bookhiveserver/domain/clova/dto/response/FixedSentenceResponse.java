package bookhive.bookhiveserver.domain.clova.dto.response;

import lombok.Getter;

@Getter
public class FixedSentenceResponse {
    private String content;

    public FixedSentenceResponse(String content) {
        this.content = content;
    }
}
