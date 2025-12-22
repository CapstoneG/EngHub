package ptit.com.enghub.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_flashcard_progress",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "flashcard_id"})
        },
        indexes = {
                @Index(name = "idx_user_flashcard", columnList = "user_id, flashcard_id"),
                @Index(name = "idx_user_next_review", columnList = "user_id, next_review_at")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFlashcardProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flashcard_id", nullable = false)
    private Flashcard flashcard;

    @Builder.Default
    @Column(nullable = false)
    private double easeFactor = 2.5;

    @Builder.Default
    @Column(nullable = false)
    private int repetitions = 0;

    @Builder.Default
    @Column(nullable = false)
    private int intervalDays = 0;

    private LocalDateTime nextReviewAt;
    private LocalDateTime lastReviewedAt;
}
