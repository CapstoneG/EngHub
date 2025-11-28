package ptit.com.enghub.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@Table(name = "notifications")
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id = UUID.randomUUID();

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "title")
    private String title;

    @Column(name = "content", columnDefinition = "text")
    private String content;

    @Column(name = "is_read")
    private boolean isRead = false;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @Column(name = "read_at")
    private Instant readAt;
}

