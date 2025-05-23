package bookhive.bookhiveserver.domain.book.repository;

import bookhive.bookhiveserver.domain.book.entity.Book;
import bookhive.bookhiveserver.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByUserAndIsbn(User user, String isbn);
    Optional<Book> findByUserAndTitleAndAuthor(User user, String title, String author);
    List<Book> findAllByUserOrderByCreatedAtDesc(User user);
}
