package ptit.com.enghub.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ptit.com.enghub.entity.Notification;

import java.time.Instant;
import java.util.UUID;

public interface NotificationRepository
        extends JpaRepository<Notification, UUID> {
    Page<Notification> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);
    long countByUserIdAndIsReadFalse(String userId);

    @Modifying
    @Query("""
        UPDATE Notification n
        SET n.isRead = true,
            n.readAt = :readAt
        WHERE n.userId = :userId
          AND n.isRead = false
    """)
    void markAllAsRead(
            @Param("userId") String userId,
            @Param("readAt") Instant readAt
    );
}
