package bookhive.bookhiveserver.domain.ai.service.content;

import bookhive.bookhiveserver.domain.ai.dto.request.RecommendTagsRequest;
import bookhive.bookhiveserver.domain.ai.dto.response.RecommendTagResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ContentFacade {

    private final ContentService contentService;

    public List<RecommendTagResponse> recommendTagsPersonalized(RecommendTagsRequest request, String token) {

        List<String> sortedTags = contentService.sortTagsByContentRelevance(request, token);
        Mono<List<String>> relevantTagsMono = contentService.recommendRelevantOriginTags(sortedTags, request, token);
        Mono<List<String>> newTagsMono = contentService.recommendRelevantNewTags(request, token);

        List<String> resultTags = Mono.zip(relevantTagsMono, newTagsMono)
                .map(tuple -> {
                    List<String> mergedTags = new ArrayList<>();
                    List<String> relevantTags = tuple.getT1();
                    List<String> newTags = tuple.getT2();
                    mergedTags.addAll(relevantTags);
                    mergedTags.addAll(newTags);
                    return mergedTags;
                })
                .block();

        return contentService.createRecommendTagList(String.join(", ", resultTags), token);
    }
}
