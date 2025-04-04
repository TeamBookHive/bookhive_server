package bookhive.bookhiveserver.domain.ai.service.content;

import bookhive.bookhiveserver.domain.ai.client.AiClient;
import bookhive.bookhiveserver.domain.ai.dto.request.CorrectTextRequest;
import bookhive.bookhiveserver.domain.ai.dto.request.RecommendTagsRequest;
import bookhive.bookhiveserver.domain.ai.dto.response.RecommendTagResponse;
import bookhive.bookhiveserver.domain.tag.entity.Tag;
import bookhive.bookhiveserver.domain.tag.repository.TagRepository;
import bookhive.bookhiveserver.domain.user.entity.User;
import bookhive.bookhiveserver.global.auth.resolver.UserResolver;
import bookhive.bookhiveserver.global.event.content.ContentFixedEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final UserResolver userResolver;
    private final AiClient aiClient;
    private final TagRepository tagRepository;

    private final ApplicationEventPublisher eventPublisher;

    public String correctText(CorrectTextRequest request, String token) {
        User user = userResolver.resolve(token);

        String correctedContent = aiClient.correct(request.getContent()).getCorrectedContent();
        eventPublisher.publishEvent(ContentFixedEvent.create(user.getId(), request.getProcessId(), correctedContent));

        return correctedContent;
    }

    public String recommendTags(RecommendTagsRequest request, String token) {
        User user = userResolver.resolve(token);

        List<String> tagNames = tagRepository.findAllByUser(user).stream()
                .map(Tag::getValue)
                .collect(Collectors.toList());

        String originTags = String.join(", ", tagNames);

        return String.join(", ", aiClient.recommendTags(request.getContent(), originTags).getTags());
    }

    public List<RecommendTagResponse> createRecommendTagList(String tagValues, String token) {
        User user = userResolver.resolve(token);

        List<RecommendTagResponse> recommendTags = new ArrayList<>();
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