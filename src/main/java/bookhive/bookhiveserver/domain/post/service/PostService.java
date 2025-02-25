package bookhive.bookhiveserver.domain.post.service;

import bookhive.bookhiveserver.domain.post.dto.PostResponse;
import bookhive.bookhiveserver.domain.post.entity.Post;
import bookhive.bookhiveserver.domain.post.repository.PostRepository;
import bookhive.bookhiveserver.domain.tag.entity.Tag;
import bookhive.bookhiveserver.domain.user.entity.User;
import bookhive.bookhiveserver.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<PostResponse> getPosts(String token) {
        User user = userRepository.findByToken(token)
                        .orElseThrow(() -> new RuntimeException("잘못된 토큰입니다."));
        List<Post> posts = postRepository.findByUserOrderByCreatedAtDesc(user);

        return posts.stream().map(PostResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public Post createPost(String content, String token) {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("잘못된 토큰입니다."));
        Post post = new Post(content, user);

        return postRepository.save(post);
    }

    @Transactional
    public void deletePost(String postId, String token) {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("잘못된 토큰입니다."));

        Post post = postRepository.findById(Long.valueOf(postId))
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시글입니다."));

        if (!post.getUser().equals(user)) {
            throw new RuntimeException("해당 게시글에 접근할 권한이 없습니다.");
        }

        postRepository.deleteById(Long.valueOf(postId));
    }
}
