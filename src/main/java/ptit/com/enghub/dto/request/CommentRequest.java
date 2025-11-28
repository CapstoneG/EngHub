package ptit.com.enghub.dto.request;

import lombok.Data;

@Data
public class CommentRequest {
    private Long lessonId;
    private Long parentId;
    private int rating;
    private String content;
}