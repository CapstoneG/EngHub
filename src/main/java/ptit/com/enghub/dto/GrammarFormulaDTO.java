package ptit.com.enghub.dto;


import lombok.Data;
import ptit.com.enghub.enums.VerbType;

import java.util.List;

@Data
public class GrammarFormulaDTO {
    private String type;
    private String formula;
    private String description;
    private VerbType verbType;
    private List<GrammarExampleDTO> examples;
}