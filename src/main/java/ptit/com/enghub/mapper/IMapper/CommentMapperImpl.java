package ptit.com.enghub.mapper.IMapper;

import org.springframework.stereotype.Component;
import ptit.com.enghub.dto.response.CommentResponse;
import ptit.com.enghub.entity.Comment;
import ptit.com.enghub.mapper.CommentMapper;

import java.util.stream.Collectors;

@Component
public class CommentMapperImpl implements CommentMapper {
    @Override
    public CommentResponse toResponse(Comment comment) {
        if (comment == null) return null;

        CommentResponse res = new CommentResponse();

        res.setId(comment.getId());
        res.setAuthorId(comment.getAuthor().getId());
        res.setAuthorName(comment.getAuthor().getFirstName() + " " + comment.getAuthor().getLastName());
        res.setRating(comment.getRating());
        res.setContent(comment.getContent());
        res.setLikes(comment.getLikes());
        res.setDate(comment.getCreatedAt() != null ? comment.getCreatedAt().toString() : null);
        res.setLiked(false);

        if (comment.getReplies() != null) {
            res.setReplies(
                    comment.getReplies().stream()
                            .map(this::toResponse)
                            .collect(Collectors.toList())
            );
        }

        return res;
    }
}