package ptit.com.enghub.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DeckStudyStatsResponse {
    private Long deckId;
    private String deckName;
    private int totalCards;
    private int learningCards;
    private int reviewCards;
    private int dueTodayCards;
    private int studiedToday;
    private double progressPercent;
    private int totalReviews;
    private LocalDateTime lastStudyAt;
}
