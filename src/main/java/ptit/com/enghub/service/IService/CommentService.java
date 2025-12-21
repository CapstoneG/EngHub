package ptit.com.enghub.service.IService;

import ptit.com.enghub.dto.request.CommentRequest;
import ptit.com.enghub.dto.response.CommentResponse;

import java.util.List;

public interface CommentService {
    CommentResponse createComment(CommentRequest request);
    List<CommentResponse> getCommentsByLesson(Long lessonId);
    void likeComment(Long commentId);
    void unlikeComment(Long commentId);
}
