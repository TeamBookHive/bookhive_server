package bookhive.bookhiveserver.domain.post.dto;

import bookhive.bookhiveserver.domain.book.dto.request.BookDetail;
import bookhive.bookhiveserver.domain.tag.dto.request.TagRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequest {
    private String content;
    private List<TagRequest> tags = new ArrayList<>();
    private BookDetail book;
    private String processId = "";
}