package bookhive.bookhiveserver.domain.book.controller;

import bookhive.bookhiveserver.domain.book.dto.request.BookCreateRequest;
import bookhive.bookhiveserver.domain.book.dto.response.BookCreateResponse;
import bookhive.bookhiveserver.domain.book.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<BookCreateResponse> create(@RequestHeader("Authorization") String token,
                                                     @RequestBody @Valid BookCreateRequest request) {
        return ResponseEntity.ok(bookService.create(request, token));
    }
}