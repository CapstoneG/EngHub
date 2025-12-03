package ptit.com.enghub.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "vocabulary")
@AllArgsConstructor
@NoArgsConstructor
public class Vocabulary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = true)
    private Lesson lesson;

    @Column(nullable = false)
    private String word;

    @Column(nullable = false)
    private String meaning;

    @Column(columnDefinition = "TEXT")
    private String example;

    private String imageUrl;
}
