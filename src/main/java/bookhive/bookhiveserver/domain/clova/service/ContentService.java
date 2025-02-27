package bookhive.bookhiveserver.domain.clova.service;

import bookhive.bookhiveserver.domain.clova.client.ClovaApiClient;
import bookhive.bookhiveserver.domain.clova.dto.ContentRequest;
import bookhive.bookhiveserver.domain.clova.dto.RecommendTagResponse;
import bookhive.bookhiveserver.domain.tag.dto.TagResponse;
import bookhive.bookhiveserver.domain.tag.entity.Tag;
import bookhive.bookhiveserver.domain.tag.repository.TagRepository;
import bookhive.bookhiveserver.domain.user.entity.User;
import bookhive.bookhiveserver.domain.user.repository.UserRepository;
import bookhive.bookhiveserver.global.exception.ErrorMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ContentService {
    private final ClovaApiClient clovaApiClient;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    public String callClovaApiToFix(ContentRequest request) {
        return clovaApiClient.callFix(request.getContent());
    }

    public String callClovaApiToRecommend(ContentRequest request, String token) {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, ErrorMessage.INVALID_TOKEN.toString()));
        List<String> tagNames = tagRepository.findAllByUser(user).stream()
                .map(Tag::getValue)
                .collect(Collectors.toList());

        String originTags = String.join(", ", tagNames);

        return clovaApiClient.callRecommend(request.getContent(), originTags);
    }

    public List<RecommendTagResponse> createRecommendTagList(String tagValues, String token) {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, ErrorMessage.INVALID_TOKEN.toString()));
        List<RecommendTagResponse> recommendTags = new ArrayList<>();
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
}
