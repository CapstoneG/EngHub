package ptit.com.enghub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptit.com.enghub.dto.request.StudySubmissionRequest;
import ptit.com.enghub.dto.response.FlashcardResponse;
import ptit.com.enghub.entity.UserFlashcardProgress;
import ptit.com.enghub.service.StudyService;

import java.util.List;

@RestController
@RequestMapping("/api/study")
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;

    @GetMapping("/session/{deckId}")
    public ResponseEntity<List<FlashcardResponse>> startSession(@PathVariable Long deckId) {
        List<FlashcardResponse> sessionCards = studyService.getStudySession(deckId);

        if (sessionCards.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(sessionCards);
    }

    @PostMapping("/submit")
    public ResponseEntity<UserFlashcardProgress> submitResult(@RequestBody StudySubmissionRequest request) {
        UserFlashcardProgress updatedCard = studyService.submitCardResult(
                request.getCardId(),
                request.getQuality()
        );
        return ResponseEntity.ok(updatedCard);
    }
}
