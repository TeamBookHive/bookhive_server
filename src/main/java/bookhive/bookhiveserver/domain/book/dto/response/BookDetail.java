package bookhive.bookhiveserver.domain.book.dto.response;

import bookhive.bookhiveserver.domain.book.entity.Book;
import lombok.Getter;

@Getter
public class BookDetail {

    private Long id;
    private String title;
    private String author;
    private String imageUrl;
    private String isbn;

    public BookDetail(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.imageUrl = book.getImageUrl();
        this.isbn = book.getIsbn();
    }
}
