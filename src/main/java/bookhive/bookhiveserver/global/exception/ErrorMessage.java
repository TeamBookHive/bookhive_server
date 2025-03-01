package bookhive.bookhiveserver.global.exception;

public enum ErrorMessage {
    TOO_MANY_LETTERS("[ERROR] 글자수를 초과하였습니다."),
    INVALID_TOKEN("[ERROR] 잘못된 토큰입니다. "),
    INVALID_POST("[ERROR] 존재하지 않는 게시글입니다. "),
    INVALID_TAG("[ERROR] 존재하지 않는 태그입니다. "),
    INVALID_API_RESPONSE("[ERROR] 현재 CLOVA API가 올바른 응답을 제공할 수 없습니다."),
    UNAUTHORIZED_POST("[ERROR] 해당 게시글에 접근 권한이 없습니다. "),
    UNAUTHORIZED_TAG("[ERROR] 해당 태그에 접근 권한이 없습니다. ");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
