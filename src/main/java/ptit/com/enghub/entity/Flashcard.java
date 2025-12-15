package ptit.com.enghub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "flashcards")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Flashcard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Content
    private String term; // Mặt trước (Tiếng Anh)
    private String phonetic; // Phiên âm
    private String definition; // Mặt sau (Tiếng Việt)
    private String partOfSpeech; // (n, v, adj...)

    @Column(columnDefinition = "TEXT")
    private String exampleSentence; // Câu ví dụ

    // --- Algorithm SRS ---
    @Builder.Default
    private Double easeFactor = 2.5;
    @Builder.Default
    private Integer intervalDays = 0;
    @Builder.Default
    private Integer repetitions = 0;

    private LocalDateTime nextReviewAt;

    @OneToMany(mappedBy = "flashcard", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<DeckFlashcard> deckFlashcards;
}
