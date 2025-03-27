package bookhive.bookhiveserver.global.suggestion.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class SuggestRequest {

    private List<String> categories;
    private String content;
}
