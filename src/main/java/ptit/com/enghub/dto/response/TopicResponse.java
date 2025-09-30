package ptit.com.enghub.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
public class TopicResponse {
    private Long id;
    private String name;
    private String description;
    private String level;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<VocabSimpleResponse> vocabs; // tránh vòng lặp
}
