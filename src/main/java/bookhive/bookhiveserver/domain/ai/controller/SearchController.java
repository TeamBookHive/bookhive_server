package bookhive.bookhiveserver.domain.ai.controller;

import bookhive.bookhiveserver.domain.ai.dto.response.clova.ClovaSearchResponse;
import bookhive.bookhiveserver.domain.ai.dto.request.clova.ClovaSearchRequest;
import bookhive.bookhiveserver.domain.ai.dto.response.clova.ClovaSearchTypeResponse;
import bookhive.bookhiveserver.domain.ai.service.SearchService;
import bookhive.bookhiveserver.domain.post.dto.PostResponse;
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
    public ResponseEntity<ClovaSearchResponse> search(@RequestHeader(value = "Authorization") String token,
                                                      @RequestBody ClovaSearchRequest request) {

        try {
            ClovaSearchTypeResponse searchType = searchService.checkSearchType(request);
            List<PostResponse> posts;

            if (Boolean.parseBoolean(searchType.getIsSearch())) {
                log.info("키워드 검색 시작");
                posts = searchService.searchByKeyword(searchType.getKeyword(), token);
            } else {
                log.info("AI 검색 시작");
                posts = searchService.searchByAI(request, token);
            }

            if (posts.isEmpty()) {
                log.info("맞는 결과 없음");
                posts = searchService.getRandomPosts(token);
                log.info("랜덤으로 조회된 게시글:{}", posts.stream().map(PostResponse::getId).toList());

                return ResponseEntity.ok(new ClovaSearchResponse(false, posts));
            }

            log.info("성공적으로 조회된 게시글:{}", posts.stream().map(PostResponse::getId).toList());

            return ResponseEntity.ok(new ClovaSearchResponse(true, posts));
        } catch (Exception e) {
            log.error("검색 중 예외 발생: {}", e.getMessage(), e);
            List<PostResponse> randomPosts = searchService.getRandomPosts(token);

            return ResponseEntity.ok(new ClovaSearchResponse(false, randomPosts));
        }
    }
}