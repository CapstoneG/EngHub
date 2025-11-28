package ptit.com.enghub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dialogue")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dialogue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @Column(nullable = false)
    private String speaker;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;
}