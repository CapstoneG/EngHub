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

    // 1. Lấy thẻ ĐẾN HẠN (Due Cards): Cần ôn ngay (Review <= Now)
    // Sắp xếp ưu tiên thẻ bị trễ hạn lâu nhất lên đầu
    // 1. Lấy thẻ ĐẾN HẠN (Due Cards): Cần ôn ngay (Review <= Now)
    // Sắp xếp ưu tiên thẻ bị trễ hạn lâu nhất lên đầu
    @Query("SELECT f FROM Flashcard f JOIN f.deckFlashcards df WHERE df.deck.id = :deckId AND f.nextReviewAt <= :now ORDER BY f.nextReviewAt ASC")
    List<Flashcard> findDueCards(Long deckId, LocalDateTime now, Pageable pageable);

    // 2. Lấy thẻ MỚI (New Cards): Chưa học bao giờ (Repetitions = 0)
    // Lấy ngẫu nhiên hoặc theo thứ tự ID
    @Query("SELECT f FROM Flashcard f JOIN f.deckFlashcards df WHERE df.deck.id = :deckId AND f.repetitions = 0")
    List<Flashcard> findNewCards(Long deckId, Pageable pageable);

    @Query("""
        SELECT f
        FROM Flashcard f
        JOIN f.deckFlashcards df
        WHERE df.deck.id = :deckId
    """)
    List<Flashcard> findByDeckId(@Param("deckId") Long deckId);
}
