package bookhive.bookhiveserver.domain.user.controller;

import bookhive.bookhiveserver.domain.user.dto.LoginResponse;
import bookhive.bookhiveserver.domain.user.entity.User;
import bookhive.bookhiveserver.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping("")
    @Operation(summary = "익명 로그인", description = "기기 정보를 통해 익명 로그인 토큰을 발급합니다. 이미 존재하는 기기인 경우 이전에 발급된 토큰을 반환합니다.")
    public ResponseEntity<LoginResponse> login(@RequestHeader("Device-Id") String deviceId, HttpServletResponse response) {
        User user = userService.login(deviceId);
        response.setHeader("Authorization", user.getToken());

        return ResponseEntity.ok(new LoginResponse(user.getId()));
    }
}
