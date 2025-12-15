package ptit.com.enghub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ptit.com.enghub.entity.Deck;

import java.util.List;

@Repository
public interface DeckRepository extends JpaRepository<Deck, Long> {

    // 1. Lấy tất cả Deck của User (Gồm cả tự tạo và Clone)
    List<Deck> findByOwnerId(Long ownerId);

    // 2. Lấy Deck hệ thống NHƯNG trừ những cái User đã Clone rồi
    // Logic: Lấy Deck Public mà ID của nó KHÔNG nằm trong danh sách sourceDeckId mà user đang sở hữu
    @Query("""
        SELECT d FROM Deck d
        WHERE d.creatorId = 1
        AND d.id NOT IN (
            SELECT my.sourceDeckId FROM Deck my WHERE my.ownerId = :userId AND my.sourceDeckId IS NOT NULL
        )
    """)
    List<Deck> findAvailableSystemDecksForUser(Long userId);
}
