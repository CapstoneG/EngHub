package ptit.com.enghub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "grammar")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Grammar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column(nullable = false)
    private String topic;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    @OneToMany(mappedBy = "grammar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GrammarFormula> formulas = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String signalWord; // signal world
}