package ptit.com.enghub.dto;

import lombok.Data;

import java.util.List;

@Data
public class GrammarDTO {
    private String topic;
    private String explanation;
    private String signalWord;
    private List<GrammarFormulaDTO> formulas;
}