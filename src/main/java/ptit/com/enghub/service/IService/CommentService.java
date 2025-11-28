package ptit.com.enghub.service.IService;

import ptit.com.enghub.dto.request.CommentRequest;
import ptit.com.enghub.dto.response.CommentResponse;

import java.util.List;

public interface CommentService {
    CommentResponse createComment(Long userId, CommentRequest request);
    List<CommentResponse> getCommentsByLesson(Long lessonId, Long currentUserId);
    void likeComment(Long userId, Long commentId);
    void unlikeComment(Long userId, Long commentId);
}
