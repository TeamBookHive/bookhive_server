package bookhive.bookhiveserver.testUtil;

import bookhive.bookhiveserver.domain.book.dto.request.BookDetail;
import bookhive.bookhiveserver.domain.post.dto.PostRequest;
import bookhive.bookhiveserver.domain.tag.dto.request.TagRequest;
import java.util.List;

public class PostDtoMother {

    public static PostRequest createPostRequest(String content, List<TagRequest> tags, BookDetail book, String processId) {
        PostRequest dto = new PostRequest();
        dto.setContent(content);
        dto.setTags(tags);
        dto.setBook(book);
        dto.setProcessId(processId);

        return dto;
    }

    public static BookDetail createBookDetail(String title, String author, String imageUrl, String isbn) {
        BookDetail dto = new BookDetail();
        dto.setTitle(title);
        dto.setAuthor(author);
        dto.setImageUrl(imageUrl);
        dto.setIsbn(isbn);

        return dto;
    }
}
