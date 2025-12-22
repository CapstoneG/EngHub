package ptit.com.enghub.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ptit.com.enghub.entity.Flashcard;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {

    @Query("""
        SELECT f
        FROM Flashcard f
        JOIN f.deckFlashcards df
        WHERE df.deck.id = :deckId
    """)
    List<Flashcard> findByDeckId(@Param("deckId") Long deckId);
}
