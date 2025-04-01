package bookhive.bookhiveserver.domain.user.service;

import bookhive.bookhiveserver.domain.user.entity.User;
import bookhive.bookhiveserver.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public String login(String deviceId) {
        return userRepository.findByDeviceId(deviceId)
                .map(User::getToken)
                .orElseGet(() -> {
                    String newToken = generateToken();
                    User newUser = User.create(deviceId, newToken);
                    userRepository.save(newUser);
                    return newToken;
                });
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}
