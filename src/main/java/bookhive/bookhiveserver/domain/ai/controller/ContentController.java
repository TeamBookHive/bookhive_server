package bookhive.bookhiveserver.domain.ai.controller;

import bookhive.bookhiveserver.domain.ai.dto.request.CorrectTextRequest;
import bookhive.bookhiveserver.domain.ai.dto.request.RecommendTagsRequest;
import bookhive.bookhiveserver.domain.ai.dto.response.CorrectTextResponse;
import bookhive.bookhiveserver.domain.ai.dto.response.RecommendTagResponse;
import bookhive.bookhiveserver.domain.ai.service.content.ContentFacade;
import bookhive.bookhiveserver.domain.ai.service.content.ContentService;
import io.swagger.v3.oas.annotations.Operation;
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
    private final ContentFacade contentFacade;

    @PostMapping("/fix-sentence")
    @Operation(summary = "AI 문장 교정", description = "입력한 문장을 교정합니다.")
    public ResponseEntity<CorrectTextResponse> correctText(@RequestHeader("Authorization") String token,
                                                           @RequestBody CorrectTextRequest request) {
        String content = contentService.correctText(request, token);

        return ResponseEntity.ok(new CorrectTextResponse(content));
    }

    @PostMapping("/recommended-tags")
    @Operation(summary = "AI 태그 추천", description = "사용자 태그 리스트를 바탕으로 입력한 내용에 어울리는 태그를 추천합니다.")
    public ResponseEntity<List<RecommendTagResponse>> recommendTags(@RequestHeader("Authorization") String token,
                                                                    @RequestBody RecommendTagsRequest request) {
        List<RecommendTagResponse> tags = contentFacade.recommendTagsPersonalized(request, token);

        return ResponseEntity.ok(tags);
    }
}
