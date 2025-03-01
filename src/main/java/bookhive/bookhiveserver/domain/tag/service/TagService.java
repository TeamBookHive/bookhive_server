package bookhive.bookhiveserver.domain.tag.service;

import bookhive.bookhiveserver.domain.tag.dto.TagRequest;
import bookhive.bookhiveserver.domain.tag.dto.TagResponse;
import bookhive.bookhiveserver.domain.tag.entity.Tag;
import bookhive.bookhiveserver.domain.tag.repository.TagRepository;
import bookhive.bookhiveserver.domain.user.entity.User;
import bookhive.bookhiveserver.domain.user.repository.UserRepository;
import bookhive.bookhiveserver.global.exception.ErrorMessage;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    public List<TagResponse> getTags(String token) {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, ErrorMessage.INVALID_TOKEN.toString()));
        List<Tag> tags = tagRepository.findAllByUserOrderByValue(user);

        return tags.stream().map(TagResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public Tag createTag(String value, String token) {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, ErrorMessage.INVALID_TOKEN.toString()));
        Tag tag = new Tag(value, user);

        // 태그 중복 로직 검증 (단, 해당 API는 현재 미사용이므로 보류)

        return tagRepository.save(tag);
    }

    @Transactional
    public List<Tag> updateTag(List<TagRequest> tagRequests, String token) {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, ErrorMessage.INVALID_TOKEN.toString()));

        // 개별 태그 길이 검사
        tagRequests.stream()
            .filter(t -> t.getValue().length() > 10)
            .findFirst()
            .ifPresent(t -> {throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.TOO_MANY_LETTERS.toString() + t.getValue());
        });

        // 성능 개선: 사용자의 태그를 미리 조회해두고, 해당 요청에서 생성되는 태그는 직접 추가
        List<Tag> currentTags = tagRepository.findAllByUser(user);
        Map<Long, Tag> tagById = currentTags.stream().collect(Collectors.toMap(Tag::getId, tag -> tag)); // 기존 태그를 위해
        Set<String> tagValues = currentTags.stream().map(Tag::getValue).collect(Collectors.toSet()); // 새로운 태그를 위해

        // 요청 내에서 중복된 value 방지
        Set<String> newTagsInRequest = new HashSet<>();

        // 새로 추가된 태그만 클라이언트에게 반환
        List<Tag> addedTags = new ArrayList<>();

        for (TagRequest tagRequest : tagRequests) {
            Long tagId = tagRequest.getId();
            String newValue = tagRequest.getValue();

            if (tagId != null) {
                Tag tag = tagById.get(tagId);
                if (tag == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.INVALID_TAG.toString() + tagRequest.getId());
                }

                if (!Objects.equals(tag.getValue(), newValue)) {
                    tag.update(newValue);
                }
            } else {
                if (!tagValues.contains(newValue) && newTagsInRequest.add(newValue)) {
                    Tag newTag = tagRepository.save(new Tag(newValue, user));
                    tagValues.add(newValue);
                    addedTags.add(newTag);
                }
            }
        }

        return addedTags;
    }


    public void deleteTag(String tagId, String token) {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, ErrorMessage.INVALID_TOKEN.toString()));

        Tag tag = tagRepository.findById(Long.valueOf(tagId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.INVALID_TAG.toString() + tagId));

        if (!Objects.equals(tag.getUser(), user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ErrorMessage.UNAUTHORIZED_TAG.toString() + tagId);
        }

        tagRepository.deleteById(Long.valueOf(tagId));
    }
}
