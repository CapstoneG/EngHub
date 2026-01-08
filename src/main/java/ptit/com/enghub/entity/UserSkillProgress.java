package ptit.com.enghub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_skill_progress",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "skill_type", "skill_id"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSkillProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "skill_type", nullable = false)
    private String  skillType;

    @Column(name = "skill_id", nullable = false)
    private Long skillId;

    private Double score;

    @Column(name = "last_attempt_at")
    private LocalDateTime lastAttemptAt;

    @Column(name = "ai_score")
    private Integer aiScore;

    @Column(columnDefinition = "TEXT")
    private String feedbackSummary;
}

