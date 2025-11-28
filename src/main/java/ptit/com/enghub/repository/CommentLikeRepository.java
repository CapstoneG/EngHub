package ptit.com.enghub.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ptit.com.enghub.entity.CommentLike;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    boolean existsByUser_IdAndComment_Id(Long userId, Long commentId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO comment_likes (user_id, comment_id) VALUES (:userId, :commentId)", nativeQuery = true)
    void insertLike(Long userId, Long commentId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CommentLike cl WHERE cl.user.id = :userId AND cl.comment.id = :commentId")
    void deleteByUserIdAndCommentId(Long userId, Long commentId);

    int countByCommentId(Long commentId);
}
