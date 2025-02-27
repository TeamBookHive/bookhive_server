package bookhive.bookhiveserver.domain.tag.service;

import bookhive.bookhiveserver.domain.tag.dto.TagRequest;
import bookhive.bookhiveserver.domain.tag.dto.TagResponse;
import bookhive.bookhiveserver.domain.tag.entity.Tag;
import bookhive.bookhiveserver.domain.tag.repository.TagRepository;
import bookhive.bookhiveserver.domain.user.entity.User;
import bookhive.bookhiveserver.domain.user.repository.UserRepository;
import bookhive.bookhiveserver.global.exception.ErrorMessage;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
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
    public void updateTag(List<TagRequest> tagRequests, String token) {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, ErrorMessage.INVALID_TOKEN.toString()));

        for (TagRequest tagRequest : tagRequests) {
            Long tagId = tagRequest.getId();
            if (tagId != null) {
                Tag tag = tagRepository.findById(tagId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.INVALID_TAG.toString() + tagRequest.getId()));

                if (!Objects.equals(tag.getValue(), tagRequest.getValue())) {
                    tag.update(tagRequest.getValue());
                }
            } else {
                tagRepository.save(new Tag(tagRequest.getValue(), user));
            }
        }
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
