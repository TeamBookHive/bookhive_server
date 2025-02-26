package bookhive.bookhiveserver.domain.clova.dto;

import bookhive.bookhiveserver.domain.tag.entity.Tag;
import lombok.Getter;

@Getter
public class RecommendTagResponse {
    private Long id;
    private String value;

    public RecommendTagResponse(Tag tag) {
        this.id = tag.getId();
        this.value = tag.getValue();
    }

    public RecommendTagResponse(Long id, String value) {
        this.id = id;
        this.value = value;
    }
}
