package bookhive.bookhiveserver.domain.book.service;

import bookhive.bookhiveserver.domain.book.dto.mapper.BookDtoMapper;
import bookhive.bookhiveserver.domain.book.dto.request.BookCreateRequest;
import bookhive.bookhiveserver.domain.book.dto.response.BookCreateResponse;
import bookhive.bookhiveserver.domain.book.dto.response.BookShowResponse;
import bookhive.bookhiveserver.domain.book.entity.Book;
import bookhive.bookhiveserver.domain.book.repository.BookRepository;
import bookhive.bookhiveserver.domain.user.entity.User;
import bookhive.bookhiveserver.global.auth.resolver.UserResolver;
import java.util.ArrayList;
import java.util.List;
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
        Book book;

        /*
         사용자의 착오로 인해 이미 저장된 책을 추가하려는 시나리오가 발생할 가능성이 있다.
         따라서, 추가 화면에서 요청한 책의 isbn으로 1차 필터링,
         isbn이 없는 책은 제목과 저자로 필터링한다.
         */
        if (request.getIsbn() != null) {
            book = bookRepository.findByIsbn(request.getIsbn())
                    .orElseGet(() -> bookRepository.save(
                            Book.create(request.getTitle(), request.getAuthor(), request.getImageUrl(), request.getIsbn(), user)));
        } else {
            book = bookRepository.findByTitleAndAuthor(request.getTitle(), request.getAuthor())
                    .orElseGet(() -> bookRepository.save(
                            Book.create(request.getTitle(), request.getAuthor(), request.getImageUrl(), request.getIsbn(), user)));
        }

        return BookDtoMapper.toBookCreateResponse(book.getId(), book.getTitle(), book.getAuthor(), book.getImageUrl(), book.getCreatedAt());
    }

    public List<BookShowResponse> findAll(String token) {
        User user = userResolver.resolve(token);

        return BookDtoMapper.toBookShowResponse(bookRepository.findAllByUserOrderByCreatedAtDesc(user)
                .orElse(new ArrayList<>()));
    }
}
