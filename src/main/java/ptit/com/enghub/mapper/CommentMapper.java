package ptit.com.enghub.mapper;

import ptit.com.enghub.dto.response.CommentResponse;
import ptit.com.enghub.entity.Comment;

public interface CommentMapper {
    CommentResponse toResponse(Comment comment);
}
