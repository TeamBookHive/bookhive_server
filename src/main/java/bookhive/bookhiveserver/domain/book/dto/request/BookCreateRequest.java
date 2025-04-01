package bookhive.bookhiveserver.domain.book.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookCreateRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String author;
}
