package ptit.com.enghub.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class DeckDashboardResponse {
    // 1. Deck Đang học & Của tôi (Bao gồm tự tạo + Clone về)
    // Sẽ hiển thị progress bar, số thẻ cần ôn
    private List<DeckSummaryResponse> myDecks;

    // 2. Deck Hệ thống (Cửa hàng)
    // Chỉ chứa các bộ user CHƯA sở hữu để user khám phá
    private List<DeckSummaryResponse> systemDecks;
}
