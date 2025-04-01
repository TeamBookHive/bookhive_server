package bookhive.bookhiveserver.domain.book.service;

import bookhive.bookhiveserver.domain.book.dto.mapper.BookDtoMapper;
import bookhive.bookhiveserver.domain.book.dto.request.BookCreateRequest;
import bookhive.bookhiveserver.domain.book.dto.response.BookCreateResponse;
import bookhive.bookhiveserver.domain.book.entity.Book;
import bookhive.bookhiveserver.domain.book.repository.BookRepository;
import bookhive.bookhiveserver.domain.user.entity.User;
import bookhive.bookhiveserver.domain.user.repository.UserRepository;
import bookhive.bookhiveserver.global.exception.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class BookService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Transactional
    public BookCreateResponse create(BookCreateRequest request, String token) {

        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, ErrorMessage.INVALID_TOKEN.toString()));

        Book book = bookRepository.findByTitleAndAuthor(request.getTitle(), request.getAuthor())
                .orElseGet(() -> bookRepository.save(Book.create(request.getTitle(), request.getAuthor(), user)));

        return BookDtoMapper.toBookCreateResponse(book.getTitle(), book.getAuthor(), book.getCreatedAt());
    }
}
