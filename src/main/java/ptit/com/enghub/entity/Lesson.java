package ptit.com.enghub.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "lesson")
@NoArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "order_index", nullable = false)
    private int orderIndex;

    @Column
    private int duration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private Unit unit;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Exercise> exercises;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Dialogue> dialogue;

    @OneToOne(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Grammar grammar;

    @OneToOne(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Video video;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vocabulary> vocabularies;

}
