package bookhive.bookhiveserver.global.suggestion.service;

import bookhive.bookhiveserver.domain.user.entity.User;
import bookhive.bookhiveserver.global.auth.resolver.UserResolver;
import bookhive.bookhiveserver.global.suggestion.dto.SuggestRequest;
import lombok.RequiredArgsConstructor;
import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SuggestionService {

    private final UserResolver userResolver;
    private final SlackApi slackApi;

    public void alert(String token, SuggestRequest request) {
        User user = userResolver.resolve(token);

        String message = String.format(
                ":mega: *사용자 %s님의 메시지 도착!*\n\n*건의 대상*\n%s\n\n*건의 내용*\n%s",
                user.getId(),
                String.join(", ", request.getCategories()),
                request.getContent()
        );

        slackApi.call(new SlackMessage(message));
    }
}
