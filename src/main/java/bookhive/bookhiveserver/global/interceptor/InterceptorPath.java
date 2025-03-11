package bookhive.bookhiveserver.global.interceptor;

import lombok.Getter;

@Getter
public enum InterceptorPath {
    API_ALL("/**"),
    API_AI_SEARCH("/api/ai-search-posts");

    private final String path;

    InterceptorPath(String path) {
        this.path = path;
    }
}
