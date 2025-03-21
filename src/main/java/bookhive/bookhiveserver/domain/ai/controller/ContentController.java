package bookhive.bookhiveserver.domain.ai.controller;

import bookhive.bookhiveserver.domain.ai.dto.request.clova.ClovaContentRequest;
import bookhive.bookhiveserver.domain.ai.dto.response.CorrectErrorsResponse;
import bookhive.bookhiveserver.domain.ai.dto.response.RecommendTagResponse;
import bookhive.bookhiveserver.domain.ai.service.ContentService;
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
    public ResponseEntity<CorrectErrorsResponse> correct(@RequestBody ClovaContentRequest request) {
        String content = contentService.callToFix(request);

        return ResponseEntity.ok(new CorrectErrorsResponse(content));
    }

    @PostMapping("/recommended-tags")
    public ResponseEntity<List<RecommendTagResponse>> recommend(@RequestHeader("Authorization") String token,
                                                       @RequestBody ClovaContentRequest request) {
        String tagValues = contentService.callToRecommend(request, token);
        List<RecommendTagResponse> tags = contentService.createRecommendTagList(tagValues, token);

        return ResponseEntity.ok(tags);
    }
}
