package ptit.com.enghub.dto.request;

import lombok.Data;

@Data
public class CompleteLessonRequest {
    private Long userId;
    private int score;
}
