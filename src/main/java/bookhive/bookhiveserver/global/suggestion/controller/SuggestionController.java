package bookhive.bookhiveserver.global.suggestion.controller;

import bookhive.bookhiveserver.global.suggestion.dto.SuggestRequest;
import bookhive.bookhiveserver.global.suggestion.service.SuggestionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SuggestionController {

    private final SuggestionService suggestionService;

    @PostMapping("/api/suggestions")
    @Operation(summary = "건의", description = "사용자가 건의함을 통해 건의 내용을 전달합니다.")
    public ResponseEntity<Void> suggest(@RequestHeader("Authorization") String token,
                                        @RequestBody SuggestRequest request) {
        suggestionService.alert(token, request);

        return ResponseEntity.ok().build();
    }
}
