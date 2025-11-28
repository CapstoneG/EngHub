package ptit.com.enghub.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
@Table(name = "vocabulary")
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
