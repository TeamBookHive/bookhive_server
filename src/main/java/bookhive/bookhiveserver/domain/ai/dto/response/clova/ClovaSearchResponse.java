package bookhive.bookhiveserver.domain.ai.dto.response.clova;

import bookhive.bookhiveserver.domain.post.dto.PostResponse;
import java.util.List;
import lombok.Getter;

@Getter
public class ClovaSearchResponse {
    private boolean searchSuccess;
    private List<PostResponse> posts;

    public ClovaSearchResponse(boolean searchSuccess, List<PostResponse> posts) {
        this.searchSuccess = searchSuccess;
        this.posts = posts;
    }
}
