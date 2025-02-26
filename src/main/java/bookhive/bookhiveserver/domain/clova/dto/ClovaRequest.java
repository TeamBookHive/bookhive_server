package bookhive.bookhiveserver.domain.clova.dto;

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
    private double topP = 0.8;
    private int topK = 0;
    private int maxTokens = 256;
    private double temperature = 0.2;
    private double repeatPenalty = 1.5;
    private List<String> stopBefore = new ArrayList<>();
    private boolean includeAiFilters = true;
    private int seed = 0;

    public ClovaRequest(List<ClovaMessage> messages) {
        this.messages = messages;
    }
}

