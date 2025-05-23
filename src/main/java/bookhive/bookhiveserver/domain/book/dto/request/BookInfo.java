package bookhive.bookhiveserver.domain.book.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookInfo {

    private Long bookId;
    private String title;
    private String author;
    private String imageUrl;
    private String isbn;
}
