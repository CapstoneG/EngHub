package ptit.com.enghub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "grammar_example")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrammarExample {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formula_id", nullable = false)
    private GrammarFormula formula;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String sentence;

    @Column(columnDefinition = "TEXT")
    private String translation;

    @Column(columnDefinition = "TEXT")
    private String highlight;
}