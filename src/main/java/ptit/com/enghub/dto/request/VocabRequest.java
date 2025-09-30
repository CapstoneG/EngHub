package ptit.com.enghub.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VocabRequest {
    private String word;
    private String phonetic;
    private String meaning;
    private String partOfSpeech;
    private String exampleSentence;
    private String imageUrl;
    private String audioUrl;
    private List<Long> topicIds; // danh sách topic gắn vào vocab

}
