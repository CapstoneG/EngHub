package ptit.com.enghub.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ptit.com.enghub.dto.request.DeckCreationRequest;
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

        public DeckSummaryResponse createDeck(Long userId, DeckCreationRequest request) {
                Deck deck = new Deck();
                deck.setName(request.getName());
                deck.setDescription(request.getDescription());
                deck.setOwnerId(userId);
                deck.setCreatorId(userId);

                Deck savedDeck = deckRepository.save(deck);
                return deckMapper.toSummaryDTO(savedDeck);
        }

        @Transactional
        public void cloneDeck(Long userId, Long originalDeckId) {
                Deck original = deckRepository.findById(originalDeckId)
                                .orElseThrow(() -> new RuntimeException("Deck not found"));

                // 1. Clone Deck Info
                Deck newDeck = Deck.builder()
                                .name(original.getName())
                                .description(original.getDescription())
                                .ownerId(userId)
                                .creatorId(original.getCreatorId())
                                .sourceDeckId(original.getId()) // Mark source
                                .build();

                // Save first to generate ID for the new Deck (needed for explicit relationship
                // if not relying purely on generic cascade)
                // Actually with Cascade.ALL we can build the graph and save once, but saving
                // deck first is safer for IDs.
                newDeck = deckRepository.save(newDeck);

                // 2. Clone Relationships (DeckFlashcard) - Reuse EXISTING Flashcards
                // We create NEW DeckFlashcard entries linking the New Deck to the Old
                // Flashcards.
                Deck finalNewDeck = newDeck;
                List<ptit.com.enghub.entity.DeckFlashcard> newDeckFlashcards = original.getDeckFlashcards().stream()
                                .map(originalDf -> {
                                        // Reuse existing flashcard
                                        Flashcard existingFlashcard = originalDf.getFlashcard();

                                        ptit.com.enghub.entity.DeckFlashcard newDf = new ptit.com.enghub.entity.DeckFlashcard();
                                        newDf.setDeck(finalNewDeck);
                                        newDf.setFlashcard(existingFlashcard);
                                        newDf.setId(new ptit.com.enghub.entity.DeckFlashcardId(finalNewDeck.getId(),
                                                        existingFlashcard.getId()));

                                        return newDf;
                                }).collect(Collectors.toList());

                // Update relationship
                if (newDeck.getDeckFlashcards() == null) {
                        newDeck.setDeckFlashcards(newDeckFlashcards);
                } else {
                        newDeck.getDeckFlashcards().addAll(newDeckFlashcards);
                }

                deckRepository.save(newDeck);
        }

        // ... method deleteDeck
}
