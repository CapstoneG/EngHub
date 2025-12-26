package ptit.com.enghub.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ptit.com.enghub.entity.Achievement;

import java.util.List;
import java.util.Optional;

public interface AchievementRepository
        extends JpaRepository<Achievement, Long> {

    @Query("""
    select a, p
    from Achievement a
    left join AchievementProgress p
        on p.achievementId = a.id
        and p.userId = :userId
    """)
    List<Object[]> findAllWithProgress(@Param("userId") Long userId);
}
