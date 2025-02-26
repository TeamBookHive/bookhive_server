package bookhive.bookhiveserver.domain.clova.controller;

import bookhive.bookhiveserver.domain.clova.dto.ContentRequest;
import bookhive.bookhiveserver.domain.clova.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ContentController {
    private final ContentService contentService;

    @PostMapping("/fix-sentence")
    public ResponseEntity<String> correct(@RequestBody ContentRequest request) {
        String content = contentService.callClovaApi(request);

        return ResponseEntity.ok(content);
    }
}
