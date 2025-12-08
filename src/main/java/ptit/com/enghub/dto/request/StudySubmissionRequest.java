package ptit.com.enghub.dto.request;

import lombok.Data;

@Data
public class StudySubmissionRequest {
    private Long cardId;

    // 1: Again (Quên - Reset)
    // 3: Good (Nhớ - Tăng Interval)
    // 5: Easy (Dễ - Tăng mạnh Interval)
    private int quality;
}
