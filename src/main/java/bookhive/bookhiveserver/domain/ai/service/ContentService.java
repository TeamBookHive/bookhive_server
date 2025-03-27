package bookhive.bookhiveserver.domain.ai.service;

import bookhive.bookhiveserver.domain.ai.client.AiClient;
import bookhive.bookhiveserver.domain.ai.dto.request.ContentRequest;
import bookhive.bookhiveserver.domain.ai.dto.response.RecommendTagResponse;
import bookhive.bookhiveserver.domain.tag.entity.Tag;
import bookhive.bookhiveserver.domain.tag.repository.TagRepository;
import bookhive.bookhiveserver.domain.user.entity.User;
import bookhive.bookhiveserver.domain.user.repository.UserRepository;
import bookhive.bookhiveserver.global.exception.ErrorMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ContentService {
    private final AiClient aiClient;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    public String callToFix(ContentRequest request) {
        return aiClient.correct(request.getContent()).getCorrectedContent();
    }

    public String callToRecommend(ContentRequest request, String token) {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, ErrorMessage.INVALID_TOKEN.toString()));
        List<String> tagNames = tagRepository.findAllByUser(user).stream()
                .map(Tag::getValue)
                .collect(Collectors.toList());

        String originTags = String.join(", ", tagNames);

        return String.join(", ", aiClient.recommendTags(request.getContent(), originTags).getTags());
    }

    public List<RecommendTagResponse> createRecommendTagList(String tagValues, String token) {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, ErrorMessage.INVALID_TOKEN.toString()));

        List<RecommendTagResponse> recommendTags = new ArrayList<>();

        // 응답 검증 로직: 응답이 잘못된 형식이면 기본 추천 태그 리스트를 반환한다.
        if (!validateTagResponse(tagValues)){
            recommendTags.add(new RecommendTagResponse(null, "일상"));
            recommendTags.add(new RecommendTagResponse(null, "기타"));

            return recommendTags;
        }

        List<String> tagValuesList =  Arrays.asList(tagValues.split(", "));

        List<Tag> tagList = tagRepository.findByValueInAndUser(tagValuesList, user);
        Map<String, Tag> tagMap = tagList.stream().collect(Collectors.toMap(Tag::getValue, tag -> tag));

        for (String tagValue : tagValuesList) {
            Tag target = tagMap.get(tagValue);
            if (target == null) {
                recommendTags.add(new RecommendTagResponse(null, tagValue));
            } else {
                recommendTags.add(new RecommendTagResponse(target));
            }
        }

        return recommendTags;
    }

    public boolean validateTagResponse(String response) {
        final Pattern TAG_PATTERN = Pattern.compile("^([가-힣a-zA-Z0-9][가-힣a-zA-Z0-9 ]*(, [가-힣a-zA-Z0-9][가-힣a-zA-Z0-9 ]*)*)$");

        return !response.equals("태그명, 태그명, 태그명") && TAG_PATTERN.matcher(response).matches();
    }
}