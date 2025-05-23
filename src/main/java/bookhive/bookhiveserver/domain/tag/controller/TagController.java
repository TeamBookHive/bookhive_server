package bookhive.bookhiveserver.domain.tag.controller;

import bookhive.bookhiveserver.domain.tag.dto.request.TagRequest;
import bookhive.bookhiveserver.domain.tag.dto.response.TagResponse;
import bookhive.bookhiveserver.domain.tag.entity.Tag;
import bookhive.bookhiveserver.domain.tag.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "태그 전체 조회", description = "사용자의 모든 태그를 조회합니다.")
    public ResponseEntity<List<TagResponse>> showTags(@RequestHeader("Authorization") String token) {
        List<TagResponse> tags = tagService.getTags(token);

        return ResponseEntity.ok(tags);
    }

    @PostMapping("")
    @Operation(summary = "태그 생성 (no usage)", description = "사용자가 태그 하나를 생성합니다. 현재 사용되고 있지 않습니다.")
    public ResponseEntity<TagResponse> createTag(@RequestHeader("Authorization") String token,
                                                 @RequestBody TagRequest request) {
        Tag tag = tagService.createTag(request.getValue(), token);

        return ResponseEntity.ok(new TagResponse(tag));
    }

    @PutMapping("")
    @Operation(summary = "태그 수정 및 생성", description = "사용자가 기존 태그를 수정하거나 새로운 태그를 생성합니다.")
    public ResponseEntity<List<TagResponse>> updateTag(@RequestHeader("Authorization") String token,
                            @RequestBody List<TagRequest> request) {
        List<Tag> newTags = tagService.updateTag(request, token);

        List<TagResponse> tags = newTags.stream()
                .map(TagResponse::new)
                .toList();

        return ResponseEntity.ok(tags);
    }

    @DeleteMapping("/{tagId}")
    @Operation(summary = "태그 삭제", description = "사용자가 특정 태그를 삭제합니다.")
    public void deleteTag(@RequestHeader("Authorization") String token,
                            @PathVariable("tagId") String tagId) {
        tagService.deleteTag(tagId, token);
    }

}
