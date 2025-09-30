package ptit.com.enghub.dto.response;

import lombok.Data;

@Data
public class VocabSimpleResponse {
    private Long id;
    private String word;
    private String meaning;
}
