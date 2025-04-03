package bookhive.bookhiveserver.domain.ai.service;

import bookhive.bookhiveserver.domain.ai.client.OpenAiClient;
import bookhive.bookhiveserver.domain.ai.dto.request.SearchRequest;
import bookhive.bookhiveserver.domain.ai.dto.response.AiKeywordsResponse;
import bookhive.bookhiveserver.domain.ai.dto.response.AiSearchTypeResponse;
import bookhive.bookhiveserver.domain.post.dto.response.PostResponse;
import bookhive.bookhiveserver.domain.post.entity.Post;
import bookhive.bookhiveserver.domain.post.repository.PostRepository;
import bookhive.bookhiveserver.domain.tag.entity.Tag;
import bookhive.bookhiveserver.domain.tag.repository.TagRepository;
import bookhive.bookhiveserver.domain.user.entity.User;
import bookhive.bookhiveserver.domain.user.repository.UserRepository;
import bookhive.bookhiveserver.global.event.search.PostSearchedEvent;
import bookhive.bookhiveserver.global.exception.ErrorMessage;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {

    private final OpenAiClient aiClient;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    private final ApplicationEventPublisher eventPublisher;

    public AiSearchTypeResponse checkSearchType(SearchRequest request, String token) {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, ErrorMessage.INVALID_TOKEN.toString()));

        eventPublisher.publishEvent(PostSearchedEvent.create(user.getId(), request.getQuestion()));

        return aiClient.checkSearchType(request.getQuestion());
    }

    public List<PostResponse> searchByKeyword(String keyword, String token) {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, ErrorMessage.INVALID_TOKEN.toString()));

        List<Post> posts = postRepository.findByUserAndKeyword(user, keyword);

        return posts.stream()
                .map(PostResponse::new)
                .toList();
    }

    public List<PostResponse> searchByAI(SearchRequest request, String token) {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, ErrorMessage.INVALID_TOKEN.toString()));

        List<String> tagNames = tagRepository.findAllByUser(user).stream()
                .map(Tag::getValue)
                .collect(Collectors.toList());
        String originTags = String.join(", ", tagNames);

        AiKeywordsResponse keywords = aiClient.extractKeywords(request.getQuestion(), originTags);

        log.info("추출한 키워드:" + keywords.getKeywords());

        List<Post> posts = postRepository.findByUser(user);

        // 키워드가 태그 리스트에 존재하거나, 내용에 포함된 게시글 조회
        Set<Post> searchedPosts = posts.stream()
                .filter(post -> keywords.getKeywords().stream()
                        .anyMatch(keyword -> {
                            Pattern pattern = Pattern.compile(Pattern.quote(keyword), Pattern.CASE_INSENSITIVE);
                            boolean contentMatches = pattern.matcher(post.getContent()).find();

                            boolean tagMatches = post.getPostTags().stream()
                                    .map(postTag -> postTag.getTag().getValue())
                                    .anyMatch(tagValue -> tagValue.equalsIgnoreCase(keyword));

                            return contentMatches || tagMatches;
                        })
                )
                .collect(Collectors.toSet());

        return searchedPosts.stream().map(PostResponse::new).toList();
    }

    public List<PostResponse> getRandomPosts(String token) {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, ErrorMessage.INVALID_TOKEN.toString()));

        List<Post> posts = postRepository.findByUser(user);
        Collections.shuffle(posts);

        return posts.stream()
                .limit(3)
                .map(PostResponse::new)
                .toList();
    }
}
