package ptit.com.enghub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ptit.com.enghub.enums.VerbType;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "grammar_formula")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrammarFormula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grammar_id", nullable = false)
    private Grammar grammar;

    @Column(nullable = false)
    private String type;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String formula;

    @Column(columnDefinition = "TEXT")
    private String description; // usage

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VerbType verbType;

    @OneToMany(mappedBy = "formula", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GrammarExample> examples = new ArrayList<>();

}
