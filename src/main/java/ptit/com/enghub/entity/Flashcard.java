package ptit.com.enghub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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

    @OneToMany(mappedBy = "flashcard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeckFlashcard> deckFlashcards;
}
