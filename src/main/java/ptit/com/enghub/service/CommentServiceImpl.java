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
import ptit.com.enghub.service.IService.CommentService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final LessonRepository lessonRepository;
    private final CommentLikeRepository likeRepository;
    private final UserService userService;
    private final CommentMapper commentMapper;

    @Override
    public CommentResponse createComment(CommentRequest request) {
        User user = userService.getCurrentUser();
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
    public List<CommentResponse> getCommentsByLesson(Long lessonId) {
        User user = userService.getCurrentUser();
        List<Comment> comments =
                commentRepository.findByLesson_IdAndParentIsNullOrderByCreatedAtDesc(lessonId);

        return comments.stream()
                .map(commentMapper::toResponse)
                .peek(res -> {
                    boolean liked = likeRepository.existsByUser_IdAndComment_Id(user.getId(), res.getId());
                    int countLikes = likeRepository.countByCommentId(res.getId());
                    res.setLiked(liked);
                    res.setLikes(countLikes);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void likeComment(Long commentId) {
        User user = userService.getCurrentUser();
        if (!likeRepository.existsByUser_IdAndComment_Id(user.getId(), commentId)) {
            likeRepository.insertLike(user.getId(), commentId);
        }
    }

    @Override
    public void unlikeComment(Long commentId) {
        User user = userService.getCurrentUser();
        likeRepository.deleteByUserIdAndCommentId(user.getId(), commentId);
    }
}
