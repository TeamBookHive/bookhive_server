package bookhive.bookhiveserver.domain.book.repository;

import bookhive.bookhiveserver.domain.book.entity.Book;
import bookhive.bookhiveserver.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);
    Optional<Book> findTopByUserOrderByCreatedAtDesc(User user);
}
