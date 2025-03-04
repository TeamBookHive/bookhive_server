package bookhive.bookhiveserver.domain.clova.dto.request;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClovaRequest {
    private List<ClovaMessage> messages;
    private double topP = 0.5;
    private int topK = 0;
    private int maxTokens = 256;
    private double temperature = 0.1;
    private double repeatPenalty = 1.0;
    private List<String> stopBefore = new ArrayList<>();
    private boolean includeAiFilters = true;
    private int seed = 1;

    public ClovaRequest(List<ClovaMessage> messages) {
        this.messages = messages;
    }
}

