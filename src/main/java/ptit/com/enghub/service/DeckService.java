package ptit.com.enghub.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ptit.com.enghub.dto.request.DeckCreationRequest;
import ptit.com.enghub.dto.response.DeckStudyStatsResponse;
import ptit.com.enghub.dto.response.DeckSummaryResponse;
import ptit.com.enghub.entity.*;
import ptit.com.enghub.mapper.DeckMapper;
import ptit.com.enghub.repository.DeckFlashcardRepository;
import ptit.com.enghub.repository.DeckRepository;
import ptit.com.enghub.repository.FlashcardRepository;
import ptit.com.enghub.repository.UserFlashcardProgressRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeckService {
        private final DeckRepository deckRepository;
        private final DeckMapper deckMapper;
        private final DeckFlashcardRepository deckFlashcardRepository;
        private final UserService userService;
        private final UserFlashcardProgressRepository progressRepository;
        private final FlashcardRepository flashcardRepository;

    @Transactional()
    public DeckSummaryResponse getDeckSummary(Long deckId) {
        User user = userService.getCurrentUser();
        Deck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new RuntimeException("Deck not found"));

        List<Long> flashcardIds = deckFlashcardRepository
                .findFlashcardIdsByDeckId(deckId);

        int totalCards = flashcardIds.size();

        if (totalCards == 0) {
            return DeckSummaryResponse.builder()
                    .id(deck.getId())
                    .name(deck.getName())
                    .totalCards(0)
                    .learnedCards(0)
                    .dueCards(0)
                    .progressPercent(0)
                    .build();
        }

        List<UserFlashcardProgress> progresses =
                progressRepository.findByUserIdAndFlashcardIdIn(
                        user.getId(), flashcardIds
                );

        int learnedCards = (int) progresses.stream()
                .filter(p -> p.getRepetitions() > 0)
                .count();

        int dueCards = (int) progresses.stream()
                .filter(p -> p.getNextReviewAt() != null
                        && p.getNextReviewAt().isBefore(LocalDateTime.now()))
                .count();

        int progressPercent = (int) ((learnedCards * 100.0) / totalCards);

        DeckSummaryResponse dto = deckMapper.toSummaryDTO(deck);
        dto.setLearnedCards(learnedCards);
        dto.setDueCards(dueCards);
        dto.setProgressPercent(progressPercent);

        return dto;
    }

        public DeckSummaryResponse createDeck(DeckCreationRequest request) {
            User user = userService.getCurrentUser();

            Deck deck = new Deck();
            deck.setName(request.getName());
            deck.setDescription(request.getDescription());
            deck.setOwnerId(user.getId());
            deck.setCreatorId(user.getId());
            Deck savedDeck = deckRepository.save(deck);
            return getDeckSummary(savedDeck.getId());
        }

    @Transactional
    public DeckSummaryResponse cloneDeck(Long originalDeckId) {
        User user = userService.getCurrentUser();

        Deck original = deckRepository.findById(originalDeckId)
                .orElseThrow(() -> new RuntimeException("Deck not found"));

        Deck newDeck = Deck.builder()
                .name(original.getName())
                .description(original.getDescription())
                .ownerId(user.getId())
                .creatorId(original.getCreatorId())
                .sourceDeckId(original.getId())
                .build();

        newDeck = deckRepository.save(newDeck);

        List<DeckFlashcard> newDeckFlashcards = new ArrayList<>();

        for (DeckFlashcard originalDf : original.getDeckFlashcards()) {
            Flashcard source = originalDf.getFlashcard();

            Flashcard clonedFlashcard = Flashcard.builder()
                    .term(source.getTerm())
                    .phonetic(source.getPhonetic())
                    .definition(source.getDefinition())
                    .partOfSpeech(source.getPartOfSpeech())
                    .exampleSentence(source.getExampleSentence())
                    .build();

            flashcardRepository.save(clonedFlashcard);

            DeckFlashcard newDf = DeckFlashcard.builder()
                    .deck(newDeck)
                    .flashcard(clonedFlashcard)
                    .build();

            newDeckFlashcards.add(newDf);

            UserFlashcardProgress progress = UserFlashcardProgress.builder()
                    .userId(user.getId())
                    .flashcard(clonedFlashcard)
                    .easeFactor(2.5)
                    .repetitions(0)
                    .intervalDays(0)
                    .nextReviewAt(null)
                    .build();

            progressRepository.save(progress);
        }

        newDeck.setDeckFlashcards(newDeckFlashcards);
        deckRepository.save(newDeck);

        return getDeckSummary(newDeck.getId());
    }


    @Transactional
        public void deleteDeck(Long id) {
            if (!deckRepository.existsById(id)) {
                    throw new RuntimeException("Deck not found");
            }

            User user = userService.getCurrentUser();
            List<Long> flashcardIds =
                    deckFlashcardRepository.findFlashcardIdsByDeckId(id);

            if (!flashcardIds.isEmpty()) {
                progressRepository.deleteByUserIdAndFlashcardIdIn(
                        user.getId(), flashcardIds
                );
            }

            deckFlashcardRepository.deleteByDeckId(id);
            deckRepository.deleteById(id);
        }

    @Transactional
    public void resetDeckProgress(Long deckId) {
        User user = userService.getCurrentUser();

        List<Long> flashcardIds = deckFlashcardRepository
                .findFlashcardIdsByDeckId(deckId);
        if (flashcardIds.isEmpty()) {
            return;
        }
        progressRepository.resetProgress(user.getId(), flashcardIds);
    }

    @Transactional()
    public DeckStudyStatsResponse getDeckStats(Long deckId) {

        User user = userService.getCurrentUser();

        Deck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new RuntimeException("Deck not found"));

        List<Flashcard> flashcards =
                flashcardRepository.findByDeckId(deckId);

        int totalCards = flashcards.size();

        if (totalCards == 0) {
            return DeckStudyStatsResponse.builder()
                    .deckId(deckId)
                    .deckName(deck.getName())
                    .totalCards(0)
                    .build();
        }

        List<Long> flashcardIds = flashcards.stream()
                .map(Flashcard::getId)
                .toList();

        List<UserFlashcardProgress> progresses =
                progressRepository
                        .findByUserIdAndFlashcardIdIn(
                                user.getId(), flashcardIds
                        );

        Map<Long, UserFlashcardProgress> progressMap =
                progresses.stream()
                        .collect(Collectors.toMap(
                                p -> p.getFlashcard().getId(),
                                p -> p
                        ));

        int learningCards = 0;
        int reviewCards = 0;
        int dueTodayCards = 0;
        int studiedToday = 0;
        int totalReviews = 0;

        LocalDateTime lastStudyAt = null;
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        for (Flashcard card : flashcards) {
            UserFlashcardProgress p = progressMap.get(card.getId());
            totalReviews += p.getRepetitions();
            if (p.getRepetitions() == 0) {
                learningCards++;
            } else {
                reviewCards++;
            }
            if ( p.getNextReviewAt() != null &&
                    !p.getNextReviewAt().isAfter(now) ) {
                dueTodayCards++;
            }
            if (p.getLastReviewedAt() != null &&
                    p.getLastReviewedAt().toLocalDate().equals(today)) {
                studiedToday++;
            }
            if (p.getLastReviewedAt() != null) {
                if (lastStudyAt == null ||
                        p.getLastReviewedAt().isAfter(lastStudyAt)) {
                    lastStudyAt = p.getLastReviewedAt();
                }
            }
        }
        double progressPercent =
                ((double) reviewCards / totalCards) * 100;

        return DeckStudyStatsResponse.builder()
                .deckId(deckId)
                .deckName(deck.getName())
                .totalCards(totalCards)
                .learningCards(learningCards)
                .reviewCards(reviewCards)
                .dueTodayCards(dueTodayCards)
                .studiedToday(studiedToday)
                .progressPercent(Math.round(progressPercent * 10.0) / 10.0)
                .totalReviews(totalReviews)
                .lastStudyAt(lastStudyAt)
                .build();
    }




}
