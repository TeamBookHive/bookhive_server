package bookhive.bookhiveserver.domain.tag.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagRequest {
    private Long id;
    private String value;
}
