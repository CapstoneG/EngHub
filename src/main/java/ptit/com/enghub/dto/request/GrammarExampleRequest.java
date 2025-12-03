package ptit.com.enghub.dto.request;

import lombok.Data;

@Data
public class GrammarExampleRequest {
    private String sentence;
    private String translation;
    private String highlight;
}