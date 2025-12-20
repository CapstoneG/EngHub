package ptit.com.enghub.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_study_daily",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_study_date",
                        columnNames = {"user_id", "study_date"}
                )
        },
        indexes = {
                @Index(name = "idx_daily_user", columnList = "user_id"),
                @Index(name = "idx_daily_study_date", columnList = "study_date"),
                @Index(name = "idx_daily_user_date", columnList = "user_id, study_date")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStudyDaily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "study_date", nullable = false)
    private LocalDate studyDate;

    @Column(name = "total_minutes", nullable = false)
    private Integer totalMinutes;

    @Column(name = "session_count", nullable = false)
    private Integer sessionCount;

    @Column(name = "first_study_at")
    private LocalDateTime firstStudyAt;

    @Column(name = "last_study_at")
    private LocalDateTime lastStudyAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
