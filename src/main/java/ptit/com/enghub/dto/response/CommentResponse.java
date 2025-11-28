package ptit.com.enghub.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class CommentResponse {
    private Long id;
    private Long authorId;
    private String authorName;
    private int rating;
    private String date;
    private String content;
    private int likes;
    private boolean liked;
    private List<CommentResponse> replies;
}
