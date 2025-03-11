package bookhive.bookhiveserver.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) {

        if (request instanceof ContentCachingRequestWrapper requestWrapper) {
            String requestBody = requestWrapper.getContentAsString();

            log.info("REQUEST METHOD: {} URI: {} BODY: {}", request.getMethod(), request.getRequestURI(), requestBody);
        }

        if (response instanceof ContentCachingResponseWrapper responseWrapper) {
            String responseBody = new String(responseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);

            log.info("RESPONSE STATUS: {} BODY: {}", response.getStatus(), responseBody);
        }
    }
}
