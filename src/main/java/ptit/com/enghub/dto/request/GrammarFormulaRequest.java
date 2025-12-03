package ptit.com.enghub.dto.request;


import lombok.Data;
import ptit.com.enghub.enums.VerbType;

import java.util.List;

@Data
public class GrammarFormulaRequest {
    private String type;
    private String formula;
    private String description;
    private VerbType verbType;
    private List<GrammarExampleRequest> examples;
}