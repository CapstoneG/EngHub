package ptit.com.enghub.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ptit.com.enghub.entity.UserStudySession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserStudySessionRepository
        extends JpaRepository<UserStudySession, Long> {

    List<UserStudySession> findByUserId(Long userId);

    List<UserStudySession> findByUserIdAndStartedAtBetween(
            Long userId,
            LocalDateTime from,
            LocalDateTime to
    );

    Optional<UserStudySession> findTopByUserIdOrderByEndedAtDesc(Long userId);
    Optional<UserStudySession> findTopByUserIdOrderByStartedAtDesc(Long userId);

    @Query("""
        SELECT COALESCE(SUM(s.durationMinutes), 0)
        FROM UserStudySession s
        WHERE s.userId = :userId
        AND s.startedAt BETWEEN :from AND :to
    """)
    int sumStudyMinutes(
            @Param("userId") Long userId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    @Query("""
        SELECT
            s.userId,
            DATE(s.startedAt),
            SUM(s.durationMinutes),
            COUNT(s),
            MIN(s.startedAt),
            MAX(s.endedAt)
        FROM UserStudySession s
        WHERE s.userId = :userId
        GROUP BY s.userId, DATE(s.startedAt)
    """)
    List<Object[]> aggregateDaily(@Param("userId") Long userId);

    List<UserStudySession> findByEndedAtIsNullAndStartedAtBefore(LocalDateTime threshold);
}

