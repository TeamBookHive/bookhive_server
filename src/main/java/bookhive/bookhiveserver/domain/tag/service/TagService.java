package bookhive.bookhiveserver.domain.tag.service;

import bookhive.bookhiveserver.domain.tag.dto.TagResponse;
import bookhive.bookhiveserver.domain.tag.entity.Tag;
import bookhive.bookhiveserver.domain.tag.repository.TagRepository;
import bookhive.bookhiveserver.domain.user.entity.User;
import bookhive.bookhiveserver.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
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
}
