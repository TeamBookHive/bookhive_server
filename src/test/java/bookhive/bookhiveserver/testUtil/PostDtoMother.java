package bookhive.bookhiveserver.testUtil;

import bookhive.bookhiveserver.domain.book.dto.request.BookInfo;
import bookhive.bookhiveserver.domain.post.dto.request.PostRequest;
import bookhive.bookhiveserver.domain.tag.dto.request.TagRequest;
import java.util.List;

public class PostDtoMother {

    public static PostRequest createPostRequest(String content, List<TagRequest> tags, BookInfo book, String processId) {
        PostRequest dto = new PostRequest();
        dto.setContent(content);
        dto.setTags(tags);
        dto.setBook(book);
        dto.setProcessId(processId);

        return dto;
    }

    public static BookInfo createBookDetail(String title, String author, String imageUrl, String isbn) {
        BookInfo dto = new BookInfo();
        dto.setTitle(title);
        dto.setAuthor(author);
        dto.setImageUrl(imageUrl);
        dto.setIsbn(isbn);

        return dto;
    }
}
