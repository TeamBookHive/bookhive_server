package bookhive.bookhiveserver.domain.book.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDetail {

    private Long id;
    private String title;
    private String author;

}
