package ptit.com.enghub.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptit.com.enghub.dto.response.FlashcardResponse;
import ptit.com.enghub.entity.Flashcard;
import ptit.com.enghub.entity.User;
import ptit.com.enghub.entity.UserFlashcardProgress;
import ptit.com.enghub.mapper.FlashcardMapper;
import ptit.com.enghub.repository.FlashcardRepository;
import ptit.com.enghub.repository.UserFlashcardProgressRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final FlashcardRepository flashcardRepository;
    private final FlashcardMapper flashcardMapper;
    private final UserService userService;
    private final UserFlashcardProgressRepository progressRepository;

    private static final int SESSION_LIMIT = 20;

    public List<FlashcardResponse> getStudySession(Long deckId) {

        User user = userService.getCurrentUser();
        LocalDateTime now = LocalDateTime.now();

        List<UserFlashcardProgress> dueProgresses =
                progressRepository.findDueCards(
                        user.getId(),
                        deckId,
                        now,
                        Pageable.ofSize(SESSION_LIMIT)
                );

        List<Flashcard> sessionCards = dueProgresses.stream()
                .map(UserFlashcardProgress::getFlashcard)
                .collect(Collectors.toCollection(ArrayList::new));

        int slotsRemaining = SESSION_LIMIT - sessionCards.size();
        if (slotsRemaining > 0) {
            List<UserFlashcardProgress> newProgresses =
                    progressRepository.findNewCards(
                            user.getId(),
                            deckId,
                            Pageable.ofSize(slotsRemaining)
                    );

            List<Flashcard> newCards = newProgresses.stream()
                    .map(UserFlashcardProgress::getFlashcard)
                    .toList();
            sessionCards.addAll(newCards);
        }

        return sessionCards.stream()
                .map(flashcardMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserFlashcardProgress submitCardResult(Long cardId, int quality) {

        User user = userService.getCurrentUser();

        Flashcard card = flashcardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        UserFlashcardProgress progress =
                progressRepository
                        .findByUserIdAndFlashcardId(user.getId(), cardId)
                        .orElseGet(() -> createNewProgress(user.getId(), card));

        applySM2(progress, quality);

        progress.setLastReviewedAt(LocalDateTime.now());
        progressRepository.save(progress);

        return progress;
    }

    private UserFlashcardProgress createNewProgress(Long userId, Flashcard card) {
        return UserFlashcardProgress.builder()
                .userId(userId)
                .flashcard(card)
                .easeFactor(2.5)
                .repetitions(0)
                .intervalDays(0)
                .nextReviewAt(LocalDateTime.now())
                .build();
    }

    private void applySM2(UserFlashcardProgress p, int quality) {

        if (quality < 3) {
            p.setRepetitions(0);
            p.setIntervalDays(1);
        } else {
            int reps = p.getRepetitions() + 1;
            p.setRepetitions(reps);

            int interval;
            if (reps == 1) {
                interval = 1;
            } else if (reps == 2) {
                interval = 6;
            } else {
                interval = (int) Math.ceil(
                        p.getIntervalDays() * p.getEaseFactor()
                );
            }
            p.setIntervalDays(interval);

            double ef = p.getEaseFactor()
                    + (0.1 - (5 - quality)
                    * (0.08 + (5 - quality) * 0.02));

            p.setEaseFactor(Math.max(ef, 1.3));
        }

        p.setNextReviewAt(
                LocalDateTime.now().plusDays(p.getIntervalDays())
        );
    }
}

