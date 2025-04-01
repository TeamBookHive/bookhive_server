package bookhive.bookhiveserver.domain.book.repository;

import bookhive.bookhiveserver.domain.book.entity.Book;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitleAndAuthor(String title, String author);
}
