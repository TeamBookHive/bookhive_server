package bookhive.bookhiveserver.domain.clova.controller;

import bookhive.bookhiveserver.domain.clova.dto.request.ContentRequest;
import bookhive.bookhiveserver.domain.clova.dto.response.FixedSentenceResponse;
import bookhive.bookhiveserver.domain.clova.dto.response.RecommendTagResponse;
import bookhive.bookhiveserver.domain.clova.service.ContentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ContentController {
    private final ContentService contentService;

    @PostMapping("/fix-sentence")
    public ResponseEntity<FixedSentenceResponse> correct(@RequestBody ContentRequest request) {
        String content = contentService.callClovaApiToFix(request);

        return ResponseEntity.ok(new FixedSentenceResponse(content));
    }

    @PostMapping("/recommended-tags")
    public ResponseEntity<List<RecommendTagResponse>> recommend(@RequestHeader("Authorization") String token,
                                                       @RequestBody ContentRequest request) {
        String tagValues = contentService.callClovaApiToRecommend(request, token);
        List<RecommendTagResponse> tags = contentService.createRecommendTagList(tagValues, token);

        return ResponseEntity.ok(tags);
    }
}
