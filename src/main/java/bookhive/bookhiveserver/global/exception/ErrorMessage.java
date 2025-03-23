package bookhive.bookhiveserver.global.exception;

public enum ErrorMessage {
    TOO_MANY_LETTERS("글자수를 초과하였습니다."),
    JSON_PARSE_ERROR("JSON을 파싱할 수 없습니다"),
    INVALID_TOKEN("잘못된 토큰입니다. "),
    INVALID_POST("존재하지 않는 게시글입니다. "),
    INVALID_TAG("존재하지 않는 태그입니다. "),
    UNAUTHORIZED_POST("해당 게시글에 접근 권한이 없습니다. "),
    UNAUTHORIZED_TAG("해당 태그에 접근 권한이 없습니다. "),
    OPEN_AI_REFUSAL("안전상의 이유로 OpenAI 응답 생성이 거절되었습니다."),
    OPEN_AI_SERVER_ERROR("OpenAI API 통신에 실패했습니다.")
    ;

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
