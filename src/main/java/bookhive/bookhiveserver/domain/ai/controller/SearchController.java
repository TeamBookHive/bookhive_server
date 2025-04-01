package bookhive.bookhiveserver.domain.ai.controller;

import bookhive.bookhiveserver.domain.ai.dto.request.SearchRequest;
import bookhive.bookhiveserver.domain.ai.dto.response.AiSearchTypeResponse;
import bookhive.bookhiveserver.domain.ai.dto.response.clova.SearchResponse;
import bookhive.bookhiveserver.domain.ai.service.SearchService;
import bookhive.bookhiveserver.domain.post.dto.PostResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SearchController {

    private final SearchService searchService;

    @PostMapping("/ai-search-posts")
    @Operation(summary = "AI 아카이브 검색", description = "입력된 질문을 분석해 어울리는 아카이브를 검색합니다.")
    public ResponseEntity<SearchResponse> search(@RequestHeader(value = "Authorization") String token,
                                                 @RequestBody SearchRequest request) {

        try {
            AiSearchTypeResponse searchType = searchService.checkSearchType(request, token);
            List<PostResponse> posts;

            if (Boolean.parseBoolean(searchType.getIsSearch())) {
                posts = searchService.searchByKeyword(searchType.getKeyword(), token);
            } else {
                posts = searchService.searchByAI(request, token);
            }

            if (posts.isEmpty()) {
                posts = searchService.getRandomPosts(token);

                return ResponseEntity.ok(new SearchResponse(false, posts));
            }

            return ResponseEntity.ok(new SearchResponse(true, posts));
        } catch (Exception e) {

            List<PostResponse> randomPosts = searchService.getRandomPosts(token);

            return ResponseEntity.ok(new SearchResponse(false, randomPosts));
        }
    }
}