package ptit.com.enghub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ptit.com.enghub.dto.response.DeckDashboardResponse;
import ptit.com.enghub.dto.response.DeckSummaryResponse;
import ptit.com.enghub.entity.Deck;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", uses = {FlashcardMapper.class})
public interface DeckMapper {

    // Mapping cơ bản
    @Mapping(target = "totalCards", expression = "java(deck.getCards() != null ? deck.getCards().size() : 0)")
    @Mapping(target = "learnedCards", source = "deck", qualifiedByName = "calculateLearned")
    @Mapping(target = "dueCards", source = "deck", qualifiedByName = "calculateDue")
    @Mapping(target = "progressPercent", source = "deck", qualifiedByName = "calculateProgress")
    DeckSummaryResponse toSummaryDTO(Deck deck);

    List<DeckSummaryResponse> toSummaryDTOs(List<Deck> decks);

    // --- Custom Logic tính toán thống kê ---

    @Named("calculateLearned")
    default int calculateLearned(Deck deck) {
        if (deck.getCards() == null) return 0;
        // Đếm số thẻ có repetition > 0 (đã học ít nhất 1 lần)
        return (int) deck.getCards().stream()
                .filter(c -> c.getRepetitions() > 0)
                .count();
    }

    @Named("calculateDue")
    default int calculateDue(Deck deck) {
        if (deck.getCards() == null) return 0;
        LocalDateTime now = LocalDateTime.now();
        // Đếm số thẻ đến hạn review
        return (int) deck.getCards().stream()
                .filter(c -> c.getNextReviewAt() != null && c.getNextReviewAt().isBefore(now))
                .count();
    }

    @Named("calculateProgress")
    default int calculateProgress(Deck deck) {
        if (deck.getCards() == null || deck.getCards().isEmpty()) return 0;
        long learned = deck.getCards().stream().filter(c -> c.getRepetitions() > 0).count();
        return (int) ((learned * 100) / deck.getCards().size());
    }
}
