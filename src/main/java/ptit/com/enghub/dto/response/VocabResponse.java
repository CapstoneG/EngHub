package ptit.com.enghub.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class VocabResponse {
    private Long id;
    private String word;
    private String phonetic;
    private String meaning;
    private String partOfSpeech;
    private String exampleSentence;
    private String imageUrl;
    private String audioUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<TopicSimpleResponse> topics; // tránh vòng lặp
}
