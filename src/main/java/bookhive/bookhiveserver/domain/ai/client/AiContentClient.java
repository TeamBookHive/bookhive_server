package bookhive.bookhiveserver.domain.ai.client;

import bookhive.bookhiveserver.domain.ai.dto.response.AiCorrectResponse;
import bookhive.bookhiveserver.domain.ai.dto.response.AiRecommendTagsResponse;

public interface AiContentClient {

    AiCorrectResponse correct(String content);

    AiRecommendTagsResponse recommendTags(String content, String originTags);
}