package bookhive.bookhiveserver.domain.book.dto.mapper;

import bookhive.bookhiveserver.domain.book.dto.response.BookCreateResponse;
import java.time.LocalDateTime;

public class BookDtoMapper {

    public static BookCreateResponse toBookCreateResponse(String title, String author, LocalDateTime createdAt) {
        return BookCreateResponse.builder()
                .title(title)
                .author(author)
                .createdAt(createdAt)
                .build();
    }
}
