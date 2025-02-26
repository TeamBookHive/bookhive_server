package bookhive.bookhiveserver.domain.tag.service;

import static bookhive.bookhiveserver.global.exception.ErrorMessage.INVALID_TAG;
import static bookhive.bookhiveserver.global.exception.ErrorMessage.INVALID_TOKEN;
import static bookhive.bookhiveserver.global.exception.ErrorMessage.UNAUTHORIZED_TAG;

import bookhive.bookhiveserver.domain.tag.dto.TagRequest;
import bookhive.bookhiveserver.domain.tag.dto.TagResponse;
import bookhive.bookhiveserver.domain.tag.entity.Tag;
import bookhive.bookhiveserver.domain.tag.repository.TagRepository;
import bookhive.bookhiveserver.domain.user.entity.User;
import bookhive.bookhiveserver.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    public List<TagResponse> getTags(String token) {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException(INVALID_TOKEN.toString()));
        List<Tag> tags = tagRepository.findAllByUserOrderByValue(user);

        return tags.stream().map(TagResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public Tag createTag(String value, String token) {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException(INVALID_TOKEN.toString()));
        Tag tag = new Tag(value, user);

        return tagRepository.save(tag);
    }

    @Transactional
    public void updateTag(List<TagRequest> tagRequests, String token) {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException(INVALID_TOKEN.toString()));

        for (TagRequest tagRequest : tagRequests) {
            Long tagId = tagRequest.getId();
            if (tagId != null) {
                Tag tag = tagRepository.findById(tagId)
                        .orElseThrow(() -> new RuntimeException(INVALID_TAG.toString() + tagRequest.getId()));

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
                .orElseThrow(() -> new RuntimeException(INVALID_TOKEN.toString()));

        Tag tag = tagRepository.findById(Long.valueOf(tagId))
                .orElseThrow(() -> new RuntimeException(INVALID_TAG.toString() + tagId));

        if (!Objects.equals(tag.getUser(), user)) {
            throw new RuntimeException(UNAUTHORIZED_TAG.toString() + tagId);
        }

        tagRepository.deleteById(Long.valueOf(tagId));
    }
}
