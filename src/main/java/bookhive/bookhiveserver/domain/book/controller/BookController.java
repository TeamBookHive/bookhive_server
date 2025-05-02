package bookhive.bookhiveserver.domain.book.controller;

import bookhive.bookhiveserver.domain.book.dto.request.BookCreateRequest;
import bookhive.bookhiveserver.domain.book.dto.response.BookCreateResponse;
import bookhive.bookhiveserver.domain.book.dto.response.BookShowDetailResponse;
import bookhive.bookhiveserver.domain.book.dto.response.BookShowResponse;
import bookhive.bookhiveserver.domain.book.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @PostMapping("")
    @Operation(summary = "책 추가", description = "입력받은 책의 제목과 저자를 사용자의 책 데이터로 추가합니다.")
    public ResponseEntity<BookCreateResponse> create(@RequestHeader("Authorization") String token,
                                                     @RequestBody @Valid BookCreateRequest request) {
        return ResponseEntity.ok(bookService.create(request, token));
    }

    @GetMapping("")
    @Operation(summary = "책 전체 조회", description = "사용자가 저장한 모든 책을 조회합니다.")
    public ResponseEntity<List<BookShowResponse>> show(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(bookService.findAll(token));
    }

    @GetMapping("/{bookId}")
    @Operation(summary = "책 상세 페이지", description = "사용자의 특정 책에 대한 모든 아카이브를 조회합니다.")
    public ResponseEntity<BookShowDetailResponse> showDetail(@RequestHeader("Authorization") String token,
                                                             @PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.showDetail(token, bookId));
    }

    @DeleteMapping("/{bookId}")
    @Operation(summary = "책 삭제", description = "사용자의 특정 책을 삭제합니다.")
    public ResponseEntity<Void> delete(@RequestHeader("Authorization") String token,
                                       @PathVariable Long bookId) {
        bookService.delete(token, bookId);
        return ResponseEntity.noContent().build();
    }
}