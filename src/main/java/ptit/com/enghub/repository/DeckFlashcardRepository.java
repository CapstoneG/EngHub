package ptit.com.enghub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ptit.com.enghub.entity.DeckFlashcard;

@Repository
public interface DeckFlashcardRepository extends JpaRepository<DeckFlashcard, Long> {
    @Modifying
    @Query("DELETE FROM DeckFlashcard df WHERE df.deck.id = :deckId")
    void deleteByDeckId(@Param("deckId") Long deckId);
}
