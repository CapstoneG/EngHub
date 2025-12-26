package ptit.com.enghub.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class StudySubmissionRequest {
    private List<CardResult> results;
    public static class CardResult {
        private Long cardId;
        private int quality;

        public Long getCardId() {
            return cardId;
        }
        public void setCardId(Long cardId) {
            this.cardId = cardId;
        }
        public int getQuality() {
            return quality;
        }
        public void setQuality(int quality) {
            this.quality = quality;
        }
    }
}
