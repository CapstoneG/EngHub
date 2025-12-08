package ptit.com.enghub.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ptit.com.enghub.dto.response.DeckSummaryResponse;
import ptit.com.enghub.entity.Deck;
import ptit.com.enghub.entity.Flashcard;
import ptit.com.enghub.mapper.DeckMapper;
import ptit.com.enghub.repository.DeckRepository;
import ptit.com.enghub.repository.FlashcardRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeckService {
    private final DeckRepository deckRepository;
    private final FlashcardRepository flashcardRepository;
    private final DeckMapper deckMapper;

//    public DeckSummaryResponse createDeck(Long userId, DeckCreationRequest request) {
//        Deck deck = new Deck();
//        deck.setName(request.getName());
//        deck.setDescription(request.getDescription());
//        deck.setPublic(request.isPublic());
//        deck.setOwnerId(userId);
//        deck.setCreatorId(userId);
//        // sourceDeckId để null
//
//        Deck savedDeck = deckRepository.save(deck);
//        return deckMapper.toSummaryDTO(savedDeck);
//    }

    @Transactional
    public void cloneDeck(Long userId, Long originalDeckId) {
        Deck original = deckRepository.findById(originalDeckId)
                .orElseThrow(() -> new RuntimeException("Deck not found"));

        // 1. Clone Deck Info
        Deck newDeck = Deck.builder()
                .name(original.getName())
                .description(original.getDescription())
                .isPublic(false) // Clone về thì thành private
                .ownerId(userId)
                .creatorId(original.getCreatorId())
                .sourceDeckId(original.getId()) // Đánh dấu nguồn gốc
                .build();
        newDeck = deckRepository.save(newDeck);

        // 2. Clone All Cards (Reset SRS params)
        Deck finalNewDeck = newDeck;
        List<Flashcard> newCards = original.getCards().stream().map(oldCard ->
                Flashcard.builder()
                        .term(oldCard.getTerm())
                        .phonetic(oldCard.getPhonetic())
                        .definition(oldCard.getDefinition())
                        .partOfSpeech(oldCard.getPartOfSpeech())
                        .exampleSentence(oldCard.getExampleSentence())
                        .deck(finalNewDeck) // Gán vào deck mới
                        // Reset thuật toán
                        .repetitions(0).intervalDays(0).easeFactor(2.5)
                        .nextReviewAt(LocalDateTime.now()) // Học ngay
                        .build()
        ).collect(Collectors.toList());

        flashcardRepository.saveAll(newCards);
    }

    // ... method deleteDeck
}
