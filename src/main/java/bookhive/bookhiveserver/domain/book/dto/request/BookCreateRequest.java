package bookhive.bookhiveserver.domain.book.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookCreateRequest {

    @NotBlank(message = "책 제목은 필수로 입력해야 합니다.")
    private String title;

    @NotBlank(message = "책 저자는 필수로 입력해야 합니다.")
    private String author;

    @Size(max = 2048, message = "이미지 URL은 2048자 이하만 가능합니다.")
    private String imageUrl;
}
