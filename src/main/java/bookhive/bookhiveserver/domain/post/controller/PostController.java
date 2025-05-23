package bookhive.bookhiveserver.domain.post.controller;

import bookhive.bookhiveserver.domain.post.dto.mapper.PostDtoMapper;
import bookhive.bookhiveserver.domain.post.dto.request.PostRequest;
import bookhive.bookhiveserver.domain.post.dto.response.PostCreateResponse;
import bookhive.bookhiveserver.domain.post.dto.response.PostResponse;
import bookhive.bookhiveserver.domain.post.entity.Post;
import bookhive.bookhiveserver.domain.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    @GetMapping("")
    @Operation(summary = "아카이브 전체 조회", description = "사용자의 모든 아카이브를 조회합니다.")
    public ResponseEntity<List<PostResponse>> showPosts(@RequestHeader("Authorization") String token) {
        List<PostResponse> posts = postService.getPosts(token);

        return ResponseEntity.ok(posts);
    }

    @PostMapping("")
    @Operation(summary = "아카이브 생성", description = "사용자가 아카이브를 생성합니다.")
    public ResponseEntity<PostCreateResponse> createPost(@RequestHeader("Authorization") String token,
                                                                  @RequestBody PostRequest request) {
        Post post = postService.createPost(request, token);

        return ResponseEntity.ok(PostDtoMapper.toPostCreateResponse(post));
    }

    @PutMapping("/{postId}")
    @Operation(summary = "아카이브 수정", description = "사용자가 특정 아카이브의 정보를 수정합니다.")
    public ResponseEntity<PostResponse> updatePost(@RequestHeader("Authorization") String token,
                             @RequestBody PostRequest request,
                             @PathVariable("postId") String postId) {
        Post post = postService.updatePost(postId, request.getContent(), request.getTags(), request.getBook(), token);
        return ResponseEntity.ok(new PostResponse(post));
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "아카이브 삭제", description = "사용자가 특정 아카이브를 삭제합니다.")
    public void deletePost(@RequestHeader("Authorization") String token,
                             @PathVariable("postId") String postId) {

        postService.deletePost(postId, token);
    }
}