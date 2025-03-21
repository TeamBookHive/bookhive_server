package bookhive.bookhiveserver.domain.ai.client;

import bookhive.bookhiveserver.domain.ai.dto.response.AiKeywordsResponse;
import bookhive.bookhiveserver.domain.ai.dto.response.AiSearchTypeResponse;

public interface AiSearchClient {

    AiSearchTypeResponse checkSearchType(String question);

    AiKeywordsResponse extractKeywords(String question, String originTags);
}
