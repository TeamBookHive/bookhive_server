package bookhive.bookhiveserver.domain.clova.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClovaMessage {
    private String role;
    private String content;
}
