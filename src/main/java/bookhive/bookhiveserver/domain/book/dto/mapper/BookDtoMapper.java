package bookhive.bookhiveserver.domain.book.dto.mapper;

import bookhive.bookhiveserver.domain.book.dto.response.BookCreateResponse;
import bookhive.bookhiveserver.domain.book.dto.response.BookDetail;
import bookhive.bookhiveserver.domain.book.dto.response.BookShowDetailResponse;
import bookhive.bookhiveserver.domain.book.dto.response.BookShowResponse;
import bookhive.bookhiveserver.domain.book.entity.Book;
import bookhive.bookhiveserver.domain.post.dto.response.PostDetail;
import bookhive.bookhiveserver.domain.post.entity.Post;
import java.util.List;

public class BookDtoMapper {

    public static BookCreateResponse toBookCreateResponse(Book book) {
        return BookCreateResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .imageUrl(book.getImageUrl())
                .isbn(book.getIsbn())
                .createdAt(book.getCreatedAt())
                .build();
    }

    public static List<BookShowResponse> toBookShowResponse(List<Book> books) {
        return books.stream()
                .map(book -> BookShowResponse.builder()
                        .id(book.getId())
                        .title(book.getTitle())
                        .author(book.getAuthor())
                        .imageUrl(book.getImageUrl())
                        .createdAt(book.getCreatedAt())
                        .build())
                .toList();
    }

    public static BookShowDetailResponse toBookShowDetailResponse(Book book, List<Post> posts) {
        BookDetail bookDetail = new BookDetail(book);
        List<PostDetail> postDetails = posts.stream()
                .map(PostDetail::new)
                .toList();
        return BookShowDetailResponse.builder()
                .book(bookDetail)
                .posts(postDetails)
                .build();
    }
}
