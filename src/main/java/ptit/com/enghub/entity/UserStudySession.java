package ptit.com.enghub.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ptit.com.enghub.enums.StudyActivityType;
import ptit.com.enghub.enums.StudySkill;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_study_session",
        indexes = {
                @Index(name = "idx_session_user", columnList = "user_id"),
                @Index(name = "idx_session_started_at", columnList = "started_at"),
                @Index(name = "idx_session_user_started", columnList = "user_id, started_at")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStudySession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_type", length = 30)
    private StudyActivityType activityType;

    @Enumerated(EnumType.STRING)
    @Column(name = "skill", length = 30)
    private StudySkill skill;

    @Column(name = "lesson_id")
    private Long lessonId;

    @Column(name = "deck_id")
    private Long deckId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
