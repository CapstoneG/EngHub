package ptit.com.enghub.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class FlashcardResponse {
    private Long id;

    // --- Mặt trước ---
    private String term; // Tiếng Anh
    private String phonetic; // Phiên âm

    // --- Mặt sau ---
    private String definition; // Nghĩa Tiếng Việt
    private String partOfSpeech; // Từ loại (n, v, adj)
    private String exampleSentence; // Câu ví dụ

    // --- Meta Data (Để UI xử lý logic hiển thị nếu cần) ---
    private List<Long> deckIds;
    private LocalDateTime nextReviewAt; // Để biết khi nào ôn lại
}
