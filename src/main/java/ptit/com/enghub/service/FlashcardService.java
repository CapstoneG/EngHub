package ptit.com.enghub.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ptit.com.enghub.dto.request.FlashcardRequest;
import ptit.com.enghub.dto.response.FlashcardResponse;
import ptit.com.enghub.entity.Deck;
import ptit.com.enghub.entity.DeckFlashcard;
import ptit.com.enghub.entity.Flashcard;
import ptit.com.enghub.mapper.FlashcardMapper;
import ptit.com.enghub.repository.DeckRepository;
import ptit.com.enghub.repository.FlashcardRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlashcardService {

    private final FlashcardRepository flashcardRepository;
    private final DeckRepository deckRepository;
    private final FlashcardMapper flashcardMapper;

    // 1. Create Flashcard
    @Transactional
    public FlashcardResponse createFlashcard(FlashcardRequest request) {
        Flashcard flashcard = flashcardMapper.toEntity(request);
        // Default values usually handled by mapper or entity builder defaults, but
        // double check
        // Mapper sets repetitions=0, interval=0, ease=2.5

        if (request.getDeckId() != null) {
            Deck deck = deckRepository.findById(request.getDeckId())
                    .orElseThrow(() -> new RuntimeException("Deck not found with id: " + request.getDeckId()));

            DeckFlashcard deckFlashcard = new DeckFlashcard();
            deckFlashcard.setDeck(deck);
            deckFlashcard.setFlashcard(flashcard);

            if (flashcard.getDeckFlashcards() == null) {
                flashcard.setDeckFlashcards(new ArrayList<>());
            }
            flashcard.getDeckFlashcards().add(deckFlashcard);
        }

        Flashcard savedFlashcard = flashcardRepository.save(flashcard);
        return flashcardMapper.toResponse(savedFlashcard);
    }

    // 2. Update Flashcard
    public FlashcardResponse updateFlashcard(Long id, FlashcardRequest request) {
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flashcard not found with id: " + id));

        flashcard.setTerm(request.getTerm());
        flashcard.setPhonetic(request.getPhonetic());
        flashcard.setDefinition(request.getDefinition());
        flashcard.setPartOfSpeech(request.getPartOfSpeech());
        flashcard.setExampleSentence(request.getExampleSentence());

        // Note: Generally we don't update stats (repetitions, etc.) here unless
        // specified

        Flashcard updatedFlashcard = flashcardRepository.save(flashcard);
        return flashcardMapper.toResponse(updatedFlashcard);
    }

    // 3. Delete Flashcard
    public void deleteFlashcard(Long id) {
        if (!flashcardRepository.existsById(id)) {
            throw new RuntimeException("Flashcard not found with id: " + id);
        }
        flashcardRepository.deleteById(id);
    }

    // 4. Get Flashcard
    public FlashcardResponse getFlashcard(Long id) {
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flashcard not found with id: " + id));
        return flashcardMapper.toResponse(flashcard);
    }
}
