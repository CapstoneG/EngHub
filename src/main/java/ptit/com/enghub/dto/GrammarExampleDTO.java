package ptit.com.enghub.dto;

import lombok.Data;

@Data
public class GrammarExampleDTO {
    private String sentence;
    private String translation;
    private String highlight;
}