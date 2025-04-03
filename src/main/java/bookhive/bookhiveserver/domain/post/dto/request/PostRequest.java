package bookhive.bookhiveserver.domain.post.dto.request;

import bookhive.bookhiveserver.domain.book.dto.request.BookInfo;
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
    private BookInfo book;
    private String processId = "";
}