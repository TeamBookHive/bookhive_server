package bookhive.bookhiveserver.domain.book.dto.mapper;

import bookhive.bookhiveserver.domain.book.dto.response.BookCreateResponse;
import bookhive.bookhiveserver.domain.book.dto.response.BookShowLatestResponse;
import java.time.LocalDateTime;

public class BookDtoMapper {

    public static BookCreateResponse toBookCreateResponse(Long id, String title, String author, String imageUrl, LocalDateTime createdAt) {
        return BookCreateResponse.builder()
                .id(id)
                .title(title)
                .author(author)
                .imageUrl(imageUrl)
                .createdAt(createdAt)
                .build();
    }

    public static BookShowLatestResponse toBookShowLatestResponse(Long id, String title, String author, String imageUrl, LocalDateTime createdAt) {
        return BookShowLatestResponse.builder()
                .id(id)
                .title(title)
                .author(author)
                .imageUrl(imageUrl)
                .createdAt(createdAt)
                .build();
    }
}
