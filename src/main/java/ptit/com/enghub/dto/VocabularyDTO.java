package ptit.com.enghub.dto.request;

import lombok.Data;

@Data
public class VocabularyRequest {
    private String word;
    private String meaning;
    private String example;
    private String imageUrl;
}
