package ptit.com.enghub.dto.response.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminDashboardResponse {

    private long totalUsers;
    private long totalLessons;
    private long totalSkills;
    private long activeUsers;
    private long totalDeck;

    private List<ActivityDataResponse> activityData;
    private List<ActivitySkillResponse> activityDataSkill;
}
