package bookhive.bookhiveserver.domain.post.service;

import bookhive.bookhiveserver.domain.book.entity.Book;
import bookhive.bookhiveserver.domain.book.repository.BookRepository;
import bookhive.bookhiveserver.domain.post.dto.request.PostRequest;
import bookhive.bookhiveserver.domain.post.dto.response.PostResponse;
import bookhive.bookhiveserver.domain.post.entity.Post;
import bookhive.bookhiveserver.domain.post.entity.PostTag;
import bookhive.bookhiveserver.domain.post.repository.PostRepository;
import bookhive.bookhiveserver.domain.post.repository.PostTagRepository;
import bookhive.bookhiveserver.domain.tag.dto.request.TagRequest;
import bookhive.bookhiveserver.domain.tag.entity.Tag;
import bookhive.bookhiveserver.domain.tag.repository.TagRepository;
import bookhive.bookhiveserver.domain.user.entity.User;
import bookhive.bookhiveserver.global.auth.resolver.UserResolver;
import bookhive.bookhiveserver.global.event.content.ContentSavedEvent;
import bookhive.bookhiveserver.global.event.post.PostCreatedEvent;
import bookhive.bookhiveserver.global.exception.ErrorMessage;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserResolver userResolver;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;
    private final BookRepository bookRepository;

    private final ApplicationEventPublisher eventPublisher;

    public List<PostResponse> getPosts(String token) {

        User user = userResolver.resolve(token);

        List<Post> posts = postRepository.findByUserOrderByCreatedAtDesc(user);

        return posts.stream().map(PostResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public Post createPost(PostRequest request, String token) {

        User user = userResolver.resolve(token);

        String content = request.getContent();

        if (content.length() > 1000)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.TOO_MANY_LETTERS.toString());

        List<Tag> fetchedTags = new ArrayList<>();
        Set<Long> currentTagIds = new HashSet<>();

        for (TagRequest tag : request.getTags()) {
            Long tagId = tag.getId();
            if (tagId == null) { // 태그가 존재하지 않는다면 생성하는 로직 추가
                Tag newTag = tagRepository.save(Tag.create(tag.getValue(), user));
                fetchedTags.add(newTag);
            }
            else {
                if (!currentTagIds.contains(tagId)) {
                    Tag fetchedTag = tagRepository.findById(tagId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.INVALID_TAG.toString() + tagId));
                    fetchedTags.add(fetchedTag);
                    currentTagIds.add(fetchedTag.getId());
                }
            }
        }

        Book book = null;
        if (request.getBook() != null) {
            book = bookRepository.findByIsbn(request.getBook().getIsbn())
                    .orElseGet(() -> bookRepository.save(
                            Book.create(request.getBook().getTitle(), request.getBook().getAuthor(), request.getBook().getImageUrl(), request.getBook().getIsbn(), user)));

        }

        Post post = Post.create(content, new ArrayList<>(), book, user);

        List<PostTag> postTags = fetchedTags.stream()
                .map(tag -> PostTag.create(post, tag))
                .toList();

        eventPublisher.publishEvent(PostCreatedEvent.create(user.getId()));
        eventPublisher.publishEvent(ContentSavedEvent.create(user.getId(), request.getProcessId(), content));

        return postRepository.save(post);
    }

    @Transactional
    public Post updatePost(String postId, String newContent, List<TagRequest> newTags, String token) {

        User user = userResolver.resolve(token);

        if (newContent.length() > 1000)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.TOO_MANY_LETTERS.toString());

        Post currentPost = postRepository.findById(Long.valueOf(postId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.INVALID_POST.toString() + postId));

        if (!Objects.equals(currentPost.getUser(), user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ErrorMessage.UNAUTHORIZED_POST.toString() + postId);
        }

        postTagRepository.deleteByPostId(Long.valueOf(postId));

        for (TagRequest tagRequest : newTags) {
            Tag tag = tagRepository.findById(tagRequest.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.INVALID_TAG.toString() + tagRequest.getId()));

            if (!Objects.equals(tag.getUser(), user)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, ErrorMessage.UNAUTHORIZED_TAG.toString() + tagRequest.getId());
            }

            postTagRepository.insertPostTag(Long.valueOf(postId), tagRequest.getId());
        }

        currentPost.update(newContent);
        return postRepository.save(currentPost);
    }


    @Transactional
    public void deletePost(String postId, String token) {

        User user = userResolver.resolve(token);

        Post post = postRepository.findById(Long.valueOf(postId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.INVALID_POST.toString() + postId));

        if (!Objects.equals(post.getUser(), user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ErrorMessage.UNAUTHORIZED_POST.toString() + postId);
        }

        postRepository.deleteById(Long.valueOf(postId));
    }
}
