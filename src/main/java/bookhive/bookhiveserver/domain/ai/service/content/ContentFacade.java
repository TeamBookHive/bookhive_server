package bookhive.bookhiveserver.domain.ai.service.content;

import bookhive.bookhiveserver.domain.ai.dto.request.ContentRequest;
import bookhive.bookhiveserver.domain.ai.dto.response.RecommendTagResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContentFacade {

    private final ContentService contentService;

    public List<RecommendTagResponse> recommendTagsSimple(ContentRequest request, String token) {

        String tagValues = contentService.recommendTags(request, token);

        return contentService.createRecommendTagList(tagValues, token);
    }

//    public List<RecommendTagResponse> recommendTagsAdvanced(ContentRequest request, String token) {
//
//        List<String> sortedTags = contentService.sortTagsByContentRelevance(request.get, token);
//        List<String> relatedTags = contentService.selectRelevantTags();
//        List<String> contentService.generateNewTags();
//
//        return contentService.createRecommendTagList(tagValues, token);
//    }
}
