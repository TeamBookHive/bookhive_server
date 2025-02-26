package bookhive.bookhiveserver.domain.post.service;

import bookhive.bookhiveserver.domain.post.dto.PostRequest;
import bookhive.bookhiveserver.domain.post.dto.PostResponse;
import bookhive.bookhiveserver.domain.post.entity.Post;
import bookhive.bookhiveserver.domain.post.entity.PostTag;
import bookhive.bookhiveserver.domain.post.repository.PostRepository;
import bookhive.bookhiveserver.domain.tag.entity.Tag;
import bookhive.bookhiveserver.domain.tag.repository.TagRepository;
import bookhive.bookhiveserver.domain.user.entity.User;
import bookhive.bookhiveserver.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    public List<PostResponse> getPosts(String token) {
        User user = userRepository.findByToken(token)
                        .orElseThrow(() -> new RuntimeException("잘못된 토큰입니다."));
        List<Post> posts = postRepository.findByUserOrderByCreatedAtDesc(user);

        return posts.stream().map(PostResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public Post createPost(String content, List<Tag> tags, String token) {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("잘못된 토큰입니다."));

        List<Tag> fetchedTags = tags.stream()
                .map(tag -> tagRepository.findById(tag.getId())
                        .orElseThrow(() -> new RuntimeException("존재하지 않는 태그 ID입니다: " + tag.getId())))
                .toList();

        Post post = new Post(content, new ArrayList<>(), user);

        List<PostTag> postTags = fetchedTags.stream()
                .map(tag -> new PostTag(post, tag))
                .toList();

        return postRepository.save(post);
    }

    @Transactional
    public Post updatePost(String postId, String newContent, List<Tag> newTags, String token) {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("잘못된 토큰입니다."));

        Post currentPost = postRepository.findById(Long.valueOf(postId))
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시글입니다:" + postId));

        if (!Objects.equals(currentPost.getUser(), user)) {
            throw new RuntimeException("해당 게시글에 접근할 권한이 없습니다:" + postId);
        }

        Set<Long> currentTagIds = currentPost.getPostTags().stream()
                .map(postTag -> postTag.getTag().getId())
                .collect(Collectors.toSet());

        // 새로운 태그들 중 기존에 존재하던 태그 남기기
        List<PostTag> updatedPostTags = currentPost.getPostTags().stream()
                .filter(postTag -> newTags.stream()
                        .anyMatch(tag -> tag.getId().equals(postTag.getTag().getId())))
                .collect(Collectors.toList());

        // 새로운 태그들 중 기존에 없던 태그 추가하기
        // 해당 유저에게 접근 권한이 있는 태그인지 체크하기
        for (Tag tag : newTags) {
            if (!currentTagIds.contains(tag.getId()))
                continue;

            if (!Objects.equals(tag.getUser(), user)) {
                throw new RuntimeException("해당 태그에 접근할 권한이 없습니다:" + tag.getId());
            }

            updatedPostTags.add(new PostTag(currentPost, tag));
        }

        currentPost.update(newContent, updatedPostTags);

        return postRepository.save(currentPost);
    }

    @Transactional
    public void deletePost(String postId, String token) {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("잘못된 토큰입니다."));

        Post post = postRepository.findById(Long.valueOf(postId))
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시글입니다:" + postId));

        if (!Objects.equals(post.getUser(), user)) {
            throw new RuntimeException("해당 게시글에 접근할 권한이 없습니다:" + postId);
        }

        postRepository.deleteById(Long.valueOf(postId));
    }
}
