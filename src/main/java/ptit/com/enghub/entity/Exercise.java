package ptit.com.enghub.entity;

import jakarta.persistence.*;
import lombok.*;
import ptit.com.enghub.enums.ExerciseType;

import java.util.List;

@Setter
@Getter
@Builder
@Entity
@Table(name = "exercise")
@NoArgsConstructor
@AllArgsConstructor
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String question;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExerciseType type;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String metadata;

}
