package ptit.com.enghub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ptit.com.enghub.enums.AchievementType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AchievementProgressResponse {
    private Long achievementId;
    private String achievementName;
    private AchievementType type;
    private int currentValue;
    private int targetValue;
    private boolean completed;
    private String iconUrl;
}

