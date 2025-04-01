package bookhive.bookhiveserver.domain.book.controller;

import bookhive.bookhiveserver.domain.book.dto.request.BookCreateRequest;
import bookhive.bookhiveserver.domain.book.dto.response.BookCreateResponse;
import bookhive.bookhiveserver.domain.book.dto.response.BookShowLatestResponse;
import bookhive.bookhiveserver.domain.book.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @GetMapping("/latest")
    @Operation(summary = "가장 최근 저장한 책 조회", description = "사용자가 가장 최근에 저장한 책의 제목과 저자를 반환합니다. 저장한 책이 없다면 제목, 저자의 값이 null로 반환됩니다.")
    public ResponseEntity<BookShowLatestResponse> showLatest(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(bookService.findLatestByUser(token));
    }

    @PostMapping("")
    @Operation(summary = "책 추가", description = "입력받은 책의 제목과 저자를 사용자의 책 데이터로 추가합니다. 사용자가 이미 동일한 제목과 저자의 책을 추가한 적 있다면 해당 책이 반환됩니다.")
    public ResponseEntity<BookCreateResponse> create(@RequestHeader("Authorization") String token,
                                                     @RequestBody @Valid BookCreateRequest request) {
        return ResponseEntity.ok(bookService.create(request, token));
    }
}