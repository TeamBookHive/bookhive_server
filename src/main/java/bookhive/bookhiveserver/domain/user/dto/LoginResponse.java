package bookhive.bookhiveserver.domain.user.dto;

import lombok.Getter;

@Getter
public class LoginResponse {
    private Long id;

    public LoginResponse(Long id) {
        this.id = id;
    }
}
