package ptit.com.enghub.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class GrammarRequest {
    private String topic;
    private String explanation;
    private String signalWord;
    private List<GrammarFormulaRequest> formulas;
}