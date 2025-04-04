package bookhive.bookhiveserver.domain.ai.service.content;

import bookhive.bookhiveserver.domain.ai.dto.request.RecommendTagsRequest;
import bookhive.bookhiveserver.domain.ai.dto.response.RecommendTagResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContentFacade {

    private final ContentService contentService;

    public List<RecommendTagResponse> recommendTagsSimple(RecommendTagsRequest request, String token) {
        // TO DO: User도 여기서 꺼내오면 되겠네
        // request를 그대로 넘기는 게 맞나? 그치만 파사드가 없는 애랑 통일하고 싶어.
        String tagValues = contentService.recommendTags(request, token);

        return contentService.createRecommendTagList(tagValues, token);
    }

//    public List<RecommendTagResponse> recommendTagsAdvanced(RecommendTagRequest request, String token) {
//
//        List<String> sortedTags = contentService.sortTagsByContentRelevance(request, token);
//        List<String> relatedTags = contentService.selectRelevantTags();
//        List<String> contentService.generateNewTags();
//
//        return contentService.createRecommendTagList(tagValues, token);
//    }
}
