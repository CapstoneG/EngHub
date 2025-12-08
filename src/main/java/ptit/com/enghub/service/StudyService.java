package ptit.com.enghub.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptit.com.enghub.dto.response.FlashcardResponse;
import ptit.com.enghub.entity.Flashcard;
import ptit.com.enghub.mapper.FlashcardMapper;
import ptit.com.enghub.repository.FlashcardRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final FlashcardRepository flashcardRepository;
    private final FlashcardMapper flashcardMapper; // MapStruct để convert Entity -> Response

    private static final int SESSION_LIMIT = 20; // Mỗi lần học tối đa 20 thẻ

    /**
     * Lấy danh sách thẻ cho phiên học
     */
    public List<FlashcardResponse> getStudySession(Long deckId) {
        LocalDateTime now = LocalDateTime.now();

        // Bước 1: Ưu tiên lấy thẻ Cũ cần ôn (Due Cards)
        List<Flashcard> sessionCards = new ArrayList<>(
                flashcardRepository.findDueCards(deckId, now, Pageable.ofSize(SESSION_LIMIT))
        );

        // Bước 2: Nếu thẻ cũ ít hơn giới hạn, lấp đầy bằng thẻ Mới (New Cards)
        int slotsRemaining = SESSION_LIMIT - sessionCards.size();
        if (slotsRemaining > 0) {
            List<Flashcard> newCards = flashcardRepository.findNewCards(deckId, Pageable.ofSize(slotsRemaining));
            sessionCards.addAll(newCards);
        }

        // Bước 3: Convert sang DTO để trả về Client
        return sessionCards.stream()
                .map(flashcardMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Xử lý kết quả User đánh giá (Thuật toán SM-2)
     * @param quality: 1 (Quên/Again), 3 (Khá/Good), 5 (Dễ/Easy)
     */
    @Transactional
    public FlashcardResponse submitCardResult(Long cardId, int quality) {
        Flashcard card = flashcardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found with id: " + cardId));

        // --- Bắt đầu thuật toán SM-2 ---

        if (quality < 3) {
            // Case: User QUÊN (Again)
            card.setRepetitions(0); // Reset số lần nhớ
            card.setIntervalDays(1); // Hỏi lại vào ngày mai (hoặc 10p nữa tùy config, ở đây set 1 ngày)
        } else {
            // Case: User NHỚ (Good/Easy)
            int reps = card.getRepetitions() + 1;
            card.setRepetitions(reps);

            // Tính Interval (Khoảng cách ngày ôn tiếp theo)
            int nextInterval;
            if (reps == 1) {
                nextInterval = 1;
            } else if (reps == 2) {
                nextInterval = 6;
            } else {
                // Công thức: I(n) = I(n-1) * EF
                nextInterval = (int) Math.ceil(card.getIntervalDays() * card.getEaseFactor());
            }
            card.setIntervalDays(nextInterval);

            // Cập nhật Ease Factor (Độ dễ nhớ)
            // Công thức chuẩn: EF' = EF + (0.1 - (5-q) * (0.08 + (5-q)*0.02))
            double newEf = card.getEaseFactor() + (0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02));
            if (newEf < 1.3) newEf = 1.3; // Chặn dưới không thấp hơn 1.3
            card.setEaseFactor(newEf);
        }

        // Set ngày ôn tập mới
        card.setNextReviewAt(LocalDateTime.now().plusDays(card.getIntervalDays()));

        // Lưu và trả về
        Flashcard savedCard = flashcardRepository.save(card);
        return flashcardMapper.toResponse(savedCard);
    }
}
