package bookhive.bookhiveserver.domain.tag.dto;

import bookhive.bookhiveserver.domain.tag.entity.Tag;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class TagResponse {
    private Long id;
    private String value;
    private LocalDateTime createdAt;

    public TagResponse(Tag tag) {
        this.id = tag.getId();
        this.value = tag.getValue();
        this.createdAt = tag.getCreatedAt();
    }
}
