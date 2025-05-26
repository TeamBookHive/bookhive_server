package bookhive.bookhiveserver.domain.ai.service.content;

import bookhive.bookhiveserver.domain.ai.dto.request.RecommendTagsRequest;
import bookhive.bookhiveserver.domain.ai.dto.response.RecommendTagResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ContentFacade {

    private final ContentService contentService;

    public List<RecommendTagResponse> recommendTagsPersonalized(RecommendTagsRequest request, String token) {

        List<String> sortedTags = contentService.sortTagsByContentRelevance(request, token);

        Mono<List<String>> relevantTagsMono = Optional.ofNullable(
                contentService.recommendRelevantOriginTags(sortedTags, request, token)
        ).orElse(Mono.just(List.of()));

        Mono<List<String>> newTagsMono = Optional.ofNullable(
                contentService.recommendRelevantNewTags(request, token)
        ).orElse(Mono.just(List.of()));

        List<String> resultTags = Mono.zip(relevantTagsMono, newTagsMono)
                .map(t -> Stream.concat(t.getT1().stream(), t.getT2().stream())
                        .distinct()
                        .toList())
                .block();

        return contentService.createRecommendTagList(String.join(", ", resultTags), token);
    }
}
