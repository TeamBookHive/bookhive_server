package bookhive.bookhiveserver.domain.tag.controller;

import bookhive.bookhiveserver.domain.tag.dto.TagRequest;
import bookhive.bookhiveserver.domain.tag.dto.TagResponse;
import bookhive.bookhiveserver.domain.tag.entity.Tag;
import bookhive.bookhiveserver.domain.tag.service.TagService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tags")
public class TagController {
    private final TagService tagService;

    @GetMapping("")
    public ResponseEntity<List<TagResponse>> showTags(@RequestHeader("Authorization") String token) {
        List<TagResponse> tags = tagService.getTags(token);

        return ResponseEntity.ok(tags);
    }

    @PostMapping("")
    public ResponseEntity<TagResponse> createTag(@RequestHeader("Authorization") String token,
                                                 @RequestBody TagRequest request) {
        Tag tag = tagService.createTag(request.getValue(), token);

        return ResponseEntity.ok(new TagResponse(tag));
    }

    @PutMapping("")
    public void updateTag(@RequestHeader("Authorization") String token,
                            @RequestBody List<TagRequest> request) {
        tagService.updateTag(request, token);
    }

    @DeleteMapping("/{tagId}")
    public void deleteTag(@RequestHeader("Authorization") String token,
                            @PathVariable String tagId) {
        tagService.deleteTag(tagId, token);
    }

}
