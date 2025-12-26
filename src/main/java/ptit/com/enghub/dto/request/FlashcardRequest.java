package ptit.com.enghub.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlashcardRequest {
    private Long deckId;
    private String term;
    private String phonetic;
    private String definition;
    private String partOfSpeech;
    private String exampleSentence;
}
