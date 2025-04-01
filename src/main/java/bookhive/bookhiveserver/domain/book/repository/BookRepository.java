package bookhive.bookhiveserver.domain.book.repository;

import bookhive.bookhiveserver.domain.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
