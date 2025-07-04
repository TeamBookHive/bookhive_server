package bookhive.bookhiveserver.domain.ai.dto.response;

import bookhive.bookhiveserver.domain.post.dto.response.PostResponse;
import java.util.List;
import lombok.Getter;

@Getter
public class SearchResponse {
    private boolean searchSuccess;
    private List<PostResponse> posts;

    public SearchResponse(boolean searchSuccess, List<PostResponse> posts) {
        this.searchSuccess = searchSuccess;
        this.posts = posts;
    }
}
