package bookhive.bookhiveserver.domain.tag.service;

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
                .orElseThrow(() -> new RuntimeException("잘못된 토큰입니다."));
        List<Tag> tags = tagRepository.findAllByUserOrderByValue(user);

        return tags.stream().map(TagResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public Tag createTag(String value, String token) {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("잘못된 토큰입니다."));
        Tag tag = new Tag(value, user);

        return tagRepository.save(tag);
    }

    @Transactional
    public void updateTag(List<TagRequest> tagRequests, String token) {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("잘못된 토큰입니다."));

        for (TagRequest tagRequest : tagRequests) {
            Long tagId = tagRequest.getId();
            if (tagId != null) {
                Tag tag = tagRepository.findById(tagId)
                        .orElseThrow(() -> new RuntimeException("존재하지 않는 ID의 태그입니다:" + tagRequest.getId()));

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
                .orElseThrow(() -> new RuntimeException("잘못된 토큰입니다."));

        Tag tag = tagRepository.findById(Long.valueOf(tagId))
                .orElseThrow(() -> new RuntimeException("존재하지 않는 태그입니다:" + tagId));

        if (!Objects.equals(tag.getUser(), user)) {
            throw new RuntimeException("해당 태그에 접근할 권한이 없습니다:" + tagId);
        }

        tagRepository.deleteById(Long.valueOf(tagId));
    }
}
