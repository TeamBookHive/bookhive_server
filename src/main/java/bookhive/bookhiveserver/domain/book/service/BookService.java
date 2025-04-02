package bookhive.bookhiveserver.domain.book.service;

import bookhive.bookhiveserver.domain.book.dto.mapper.BookDtoMapper;
import bookhive.bookhiveserver.domain.book.dto.request.BookCreateRequest;
import bookhive.bookhiveserver.domain.book.dto.response.BookCreateResponse;
import bookhive.bookhiveserver.domain.book.dto.response.BookShowLatestResponse;
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
                .orElseGet(() -> bookRepository.save(
                        Book.create(request.getTitle(), request.getAuthor(), request.getImageUrl(), user)));

        return BookDtoMapper.toBookCreateResponse(book.getId(), book.getTitle(), book.getAuthor(), book.getImageUrl(), book.getCreatedAt());
    }

    public BookShowLatestResponse findLatestByUser(String token) {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, ErrorMessage.INVALID_TOKEN.toString()));

        Book book = bookRepository.findTopByUserOrderByCreatedAtDesc(user)
                .orElse(null);

        if (book == null) return BookShowLatestResponse.empty();

        return BookDtoMapper.toBookShowLatestResponse(book.getId(), book.getTitle(), book.getAuthor(), book.getImageUrl(), book.getCreatedAt());
    }
}
