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

    boolean existsByUserIdAndStartedAtBetween(
            Long userId,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay
    );
}

