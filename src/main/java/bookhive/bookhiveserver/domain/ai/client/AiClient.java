package bookhive.bookhiveserver.domain.ai.client;

import bookhive.bookhiveserver.domain.ai.dto.response.AiCorrectResponse;
import bookhive.bookhiveserver.domain.ai.dto.response.AiKeywordsResponse;
import bookhive.bookhiveserver.domain.ai.dto.response.AiRecommendTagsResponse;
import bookhive.bookhiveserver.domain.ai.dto.response.AiSearchTypeResponse;

public interface AiClient {

    AiCorrectResponse correct(String content);

    AiRecommendTagsResponse recommendTags(String content, String originTags);

    AiSearchTypeResponse checkSearchType(String question);

    AiKeywordsResponse extractKeywords(String question, String originTags);
}