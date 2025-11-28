package ptit.com.enghub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ptit.com.enghub.dto.request.CommentRequest;
import ptit.com.enghub.dto.response.CommentResponse;
import ptit.com.enghub.entity.Comment;
import ptit.com.enghub.entity.Lesson;
import ptit.com.enghub.entity.User;
import ptit.com.enghub.mapper.CommentMapper;
import ptit.com.enghub.repository.CommentLikeRepository;
import ptit.com.enghub.repository.CommentRepository;
import ptit.com.enghub.repository.LessonRepository;
import ptit.com.enghub.repository.UserRepository;
import ptit.com.enghub.service.IService.CommentService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final CommentLikeRepository likeRepository;

    private final CommentMapper commentMapper;

    @Override
    public CommentResponse createComment(Long userId, CommentRequest request) {

        User user = userRepository.findById(userId).orElseThrow();
        Lesson lesson = lessonRepository.findById(request.getLessonId()).orElseThrow();

        Comment comment = Comment.builder()
                .author(user)
                .lesson(lesson)
                .parent(getParent(request.getParentId()))
                .rating(request.getRating())
                .content(request.getContent())
                .likes(0)
                .build();

        comment = commentRepository.save(comment);

        return commentMapper.toResponse(comment);
    }

    private Comment getParent(Long parentId) {
        if (parentId == null) return null;
        return commentRepository.findById(parentId).orElseThrow();
    }

    @Override
    public List<CommentResponse> getCommentsByLesson(Long lessonId, Long currentUserId) {
        List<Comment> comments =
                commentRepository.findByLesson_IdAndParentIsNullOrderByCreatedAtDesc(lessonId);

        return comments.stream()
                .map(commentMapper::toResponse)
                .peek(res -> {
                    boolean liked = likeRepository.existsByUser_IdAndComment_Id(currentUserId, res.getId());
                    int countLikes = likeRepository.countByCommentId(res.getId());
                    res.setLiked(liked);
                    res.setLikes(countLikes);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void likeComment(Long userId, Long commentId) {
        if (!likeRepository.existsByUser_IdAndComment_Id(userId, commentId)) {
            likeRepository.insertLike(userId, commentId);
        }
    }

    @Override
    public void unlikeComment(Long userId, Long commentId) {
        likeRepository.deleteByUserIdAndCommentId(userId, commentId);
    }
}
