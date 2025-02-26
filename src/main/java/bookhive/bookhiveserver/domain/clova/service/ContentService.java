package bookhive.bookhiveserver.domain.clova.service;

import bookhive.bookhiveserver.domain.clova.client.ClovaApiClient;
import bookhive.bookhiveserver.domain.clova.dto.ContentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContentService {
    private final ClovaApiClient clovaApiClient;

    public String callClovaApi(ContentRequest request) {
        return clovaApiClient.callFix(request.getContent());
    }
}
