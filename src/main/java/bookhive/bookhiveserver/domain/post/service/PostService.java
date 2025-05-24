package bookhive.bookhiveserver.domain.post.service;

import bookhive.bookhiveserver.domain.book.dto.request.BookInfo;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
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

        // 기존 로직: book이 null이 아닐 때, isbn으로 찾고 있으면 그걸 반환, 없으면 새로 저장
        // 현재 로직: book이 null이 아닐 때, id로 찾아서 그걸 반환, id가 null이면 에러 나도록
        // 과도기 로직: book이 null이 아닐 때, id가 null이더라도 동작해야 함. 일단 isbn으로 찾고 있으면 그걸 반환, 없으면 새로 저장

        Book book = null;
        BookInfo bookDto = request.getBook(); // 이것도 임시

        if (bookDto != null) {
            if (bookDto.getId() != null) {
                book = bookRepository.findById(bookDto.getId())
                        .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.INVALID_BOOK.toString()));
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.INVALID_BOOK + "책 id가 누락되었습니다.");
            }
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
    public Post updatePost(String postId, String newContent, List<TagRequest> newTags, BookInfo bookDto, String token) {

        User user = userResolver.resolve(token);

        if (newContent.length() > 1000)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.TOO_MANY_LETTERS.toString());

        Post currentPost = postRepository.findById(Long.valueOf(postId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.INVALID_POST + postId));

        if (!Objects.equals(currentPost.getUser(), user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ErrorMessage.UNAUTHORIZED_POST + postId);
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

        if (bookDto != null) {
            log.info(String.valueOf(bookDto));
            if (bookDto.getId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.INVALID_BOOK + "책 id가 누락되었습니다.");
            }
            log.info(String.valueOf(bookDto.getId()));
            Book newBook = bookRepository.findById(bookDto.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.INVALID_BOOK.toString()));
            if (currentPost.getBook() == null ||
                    !Objects.equals(currentPost.getBook().getId(), newBook.getId())) {
                currentPost.setBook(newBook);
            }
        }

        currentPost.update(newContent);
        return postRepository.save(currentPost);
    }


    @Transactional
    public void deletePost(String postId, String token) {

        User user = userResolver.resolve(token);

        Post post = postRepository.findById(Long.valueOf(postId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.INVALID_POST + postId));

        if (!Objects.equals(post.getUser(), user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ErrorMessage.UNAUTHORIZED_POST + postId);
        }

        postRepository.deleteById(Long.valueOf(postId));
    }
}
