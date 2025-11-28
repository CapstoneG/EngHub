package ptit.com.enghub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptit.com.enghub.dto.request.CommentRequest;
import ptit.com.enghub.dto.response.CommentResponse;
import ptit.com.enghub.entity.User;
import ptit.com.enghub.service.IService.CommentService;
import ptit.com.enghub.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @RequestBody CommentRequest request
    ) {
        User user = userService.getUser();
        return ResponseEntity.ok(commentService.createComment(user.getId(), request));
    }

    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<List<CommentResponse>> getComments(
            @PathVariable Long lessonId
    ) {
        User user = userService.getUser();
        return ResponseEntity.ok(commentService.getCommentsByLesson(lessonId, user.getId()));
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<Void> likeComment(
            @PathVariable Long commentId
    ) {
        User user = userService.getUser();
        commentService.likeComment(user.getId(), commentId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}/like")
    public ResponseEntity<Void> unlikeComment(
            @PathVariable Long commentId
    ) {
        User user = userService.getUser();
        commentService.unlikeComment(user.getId(), commentId);
        return ResponseEntity.ok().build();
    }
}