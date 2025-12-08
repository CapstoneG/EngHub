package ptit.com.enghub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptit.com.enghub.dto.request.StudySubmissionRequest;
import ptit.com.enghub.dto.response.FlashcardResponse;
import ptit.com.enghub.service.StudyService;

import java.util.List;

@RestController
@RequestMapping("/api/study")
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;

    // 1. Bắt đầu phiên học: Lấy danh sách thẻ (Trộn giữa thẻ Cũ và Mới)
    // GET /api/study/session/{deckId}
    @GetMapping("/session/{deckId}")
    public ResponseEntity<List<FlashcardResponse>> startSession(@PathVariable Long deckId) {
        List<FlashcardResponse> sessionCards = studyService.getStudySession(deckId);

        if (sessionCards.isEmpty()) {
            return ResponseEntity.noContent().build(); // Hoặc trả về message "Chúc mừng, bạn đã học hết!"
        }
        return ResponseEntity.ok(sessionCards);
    }

    // 2. Gửi kết quả đánh giá (Thuật toán SM-2)
    // POST /api/study/submit
    // Body: { "cardId": 101, "quality": 3 }
    @PostMapping("/submit")
    public ResponseEntity<FlashcardResponse> submitResult(@RequestBody StudySubmissionRequest request) {
        FlashcardResponse updatedCard = studyService.submitCardResult(
                request.getCardId(),
                request.getQuality()
        );
        return ResponseEntity.ok(updatedCard);
    }
}
