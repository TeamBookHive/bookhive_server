package bookhive.bookhiveserver.domain.user.controller;

import bookhive.bookhiveserver.domain.user.service.UserService;
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
    public ResponseEntity<Void> login(@RequestHeader("Device-Id") String deviceId, HttpServletResponse response) {
        String token = userService.login(deviceId);
        response.setHeader("Authorization", token);

        return ResponseEntity.ok().build();
    }
}
