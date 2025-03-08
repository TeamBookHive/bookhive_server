package bookhive.bookhiveserver.domain.clova.controller;

import bookhive.bookhiveserver.domain.clova.dto.request.SearchRequest;
import bookhive.bookhiveserver.domain.clova.dto.response.SearchTypeResponse;
import bookhive.bookhiveserver.domain.clova.service.SearchService;
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
    public ResponseEntity<List<PostResponse>> search(@RequestHeader(value = "Authorization") String token,
                                           @RequestBody SearchRequest request) {

        try {
            log.info( "키워드 검색 시작");
            SearchTypeResponse searchType = searchService.checkSearchType(request);
            List<PostResponse> posts;

            System.out.println(searchType.getIsSearch());
            System.out.println(searchType.getKeyword());

            if (Boolean.parseBoolean(searchType.getIsSearch())) {
                log.info( "키워드 검색");
                posts = searchService.searchByKeyword(searchType.getKeyword(), token);
            } else {
                log.info( "AI 검색");
                posts = searchService.searchByAI(request, token);
            }

            if (posts.isEmpty()) {
                log.info("맞는 결과 없음");
                posts = searchService.getRandomPosts(token);
            }

            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            List<PostResponse> randomPosts = searchService.getRandomPosts(token);
            return ResponseEntity.ok(randomPosts);
        }
    }
}