package ptit.com.enghub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "decks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Deck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private boolean isPublic; // True = System Deck

    @Column(name = "owner_id")
    private Long ownerId;     // Người sở hữu hiện tại (User)

    @Column(name = "source_deck_id")
    private Long sourceDeckId; // ID của bộ gốc (nếu là deck clone) - QUAN TRỌNG

    @Column(name = "creator_id")
    private Long creatorId;   // Người tạo ban đầu (User)

    @OneToMany(mappedBy = "deck", cascade = CascadeType.ALL)
    private List<Flashcard> cards;
}
