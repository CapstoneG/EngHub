package ptit.com.enghub.dto.response;

import lombok.Data;

@Data
public class DeckSummaryResponse {
    private Long id;
    private String name;
    private String description;

    // --- Các chỉ số thống kê (Calculated Fields) ---
    private int totalCards;       // Tổng số thẻ
    private int learnedCards;     // Số thẻ đã học (reps > 0)
    private int dueCards;         // Số thẻ cần ôn NGAY (để hiện badge đỏ)
    private int progressPercent;  // % hoàn thành (0 - 100)

    // --- Phân biệt nguồn gốc ---
    private boolean isPublic;     // True nếu là bộ của hệ thống
    private Long sourceDeckId;    // Nếu != null thì là bộ clone
}
