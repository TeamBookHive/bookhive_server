package bookhive.bookhiveserver.domain.post.controller;

import bookhive.bookhiveserver.domain.post.dto.PostRequest;
import bookhive.bookhiveserver.domain.post.dto.PostResponse;
import bookhive.bookhiveserver.domain.post.entity.Post;
import bookhive.bookhiveserver.domain.post.service.PostService;
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
    public ResponseEntity<List<PostResponse>> showPosts(@RequestHeader("Authorization") String token) {
        List<PostResponse> posts = postService.getPosts(token);

        return ResponseEntity.ok(posts);
    }

    @PostMapping("")
    public ResponseEntity<PostResponse> createPost(@RequestHeader("Authorization") String token,
                                           @RequestBody PostRequest request) {
        Post post = postService.createPost(request, token);

        return ResponseEntity.ok(new PostResponse(post));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(@RequestHeader("Authorization") String token,
                             @RequestBody PostRequest request,
                             @PathVariable String postId) {
        Post post = postService.updatePost(postId, request.getContent(), request.getTags(), token);
        return ResponseEntity.ok(new PostResponse(post));
    }

    @DeleteMapping("/{postId}")
    public void deletePost(@RequestHeader("Authorization") String token,
                             @PathVariable String postId) {

        postService.deletePost(postId, token);
    }
}