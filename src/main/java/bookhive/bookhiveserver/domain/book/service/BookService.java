package bookhive.bookhiveserver.domain.book.service;

import bookhive.bookhiveserver.domain.book.dto.mapper.BookDtoMapper;
import bookhive.bookhiveserver.domain.book.dto.request.BookCreateRequest;
import bookhive.bookhiveserver.domain.book.dto.response.BookCreateResponse;
import bookhive.bookhiveserver.domain.book.dto.response.BookShowLatestResponse;
import bookhive.bookhiveserver.domain.book.entity.Book;
import bookhive.bookhiveserver.domain.book.repository.BookRepository;
import bookhive.bookhiveserver.domain.user.entity.User;
import bookhive.bookhiveserver.global.auth.resolver.UserResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final UserResolver userResolver;
    private final BookRepository bookRepository;

    @Transactional
    public BookCreateResponse create(BookCreateRequest request, String token) {
        User user = userResolver.resolve(token);

        // TO DO: Isbn으로 검증 후 중복 에러 반환으로 변경
        Book book = bookRepository.findByIsbn(request.getIsbn())
                .orElseGet(() -> bookRepository.save(
                        Book.create(request.getTitle(), request.getAuthor(), request.getImageUrl(), request.getIsbn(), user)));

        return BookDtoMapper.toBookCreateResponse(book.getId(), book.getTitle(), book.getAuthor(), book.getImageUrl(), book.getCreatedAt());
    }

    public BookShowLatestResponse findLatestByUser(String token) {
        User user = userResolver.resolve(token);

        Book book = bookRepository.findTopByUserOrderByCreatedAtDesc(user)
                .orElse(null);

        if (book == null) return BookShowLatestResponse.empty();

        return BookDtoMapper.toBookShowLatestResponse(book.getId(), book.getTitle(), book.getAuthor(), book.getImageUrl(), book.getCreatedAt());
    }
}
