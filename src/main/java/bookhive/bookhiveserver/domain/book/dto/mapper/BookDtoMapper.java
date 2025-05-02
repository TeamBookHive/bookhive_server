package bookhive.bookhiveserver.domain.book.dto.mapper;

import bookhive.bookhiveserver.domain.book.dto.response.BookCreateResponse;
import bookhive.bookhiveserver.domain.book.dto.response.BookShowResponse;
import bookhive.bookhiveserver.domain.book.entity.Book;
import java.time.LocalDateTime;
import java.util.List;

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

    public static List<BookShowResponse> toBookShowResponse(List<Book> books) {
        return books.stream()
                .map(book -> BookShowResponse.builder()
                        .id(book.getId())
                        .title(book.getTitle())
                        .imageUrl(book.getImageUrl())
                        .createdAt(book.getCreatedAt())
                        .build())
                .toList();
    }
}
