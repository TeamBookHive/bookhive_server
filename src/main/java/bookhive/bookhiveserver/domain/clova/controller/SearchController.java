package bookhive.bookhiveserver.domain.clova.controller;

import bookhive.bookhiveserver.domain.clova.dto.request.ContentRequest;
import bookhive.bookhiveserver.domain.clova.service.SearchService;
import bookhive.bookhiveserver.domain.post.dto.PostResponse;
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
public class SearchController {

    private final SearchService searchService;

    @PostMapping("/ai-search-posts")
    public ResponseEntity<List<PostResponse>> search(@RequestHeader(value = "Authorization") String token,
                                                     @RequestBody ContentRequest request) {

        List<PostResponse> posts = searchService.getRandomPosts(token);

        return ResponseEntity.ok(posts);
    }
}