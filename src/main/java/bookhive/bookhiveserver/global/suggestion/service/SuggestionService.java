package bookhive.bookhiveserver.global.suggestion.service;

import bookhive.bookhiveserver.domain.user.entity.User;
import bookhive.bookhiveserver.domain.user.repository.UserRepository;
import bookhive.bookhiveserver.global.exception.ErrorMessage;
import bookhive.bookhiveserver.global.suggestion.dto.SuggestRequest;
import lombok.RequiredArgsConstructor;
import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class SuggestionService {

    private final UserRepository userRepository;
    private final SlackApi slackApi;

    public void alert(String token, SuggestRequest request) {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, ErrorMessage.INVALID_TOKEN.toString()));

        String message = String.format(
                ":mega: *사용자 %s님의 메시지 도착!*\n\n*건의 대상*\n%s\n\n*건의 내용*\n%s",
                user.getId(),
                String.join(", ", request.getCategories()),
                request.getContent()
        );

        slackApi.call(new SlackMessage(message));
    }
}
