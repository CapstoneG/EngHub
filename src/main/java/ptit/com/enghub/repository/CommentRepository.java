package ptit.com.enghub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptit.com.enghub.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByLesson_Id(Long lessonId);
    List<Comment> findByParent_Id(Long parentId);
    List<Comment> findByLesson_IdAndParentIsNullOrderByCreatedAtDesc(Long lessonId);
}