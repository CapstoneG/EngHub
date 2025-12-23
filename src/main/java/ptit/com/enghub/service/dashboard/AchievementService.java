package ptit.com.enghub.service.dashboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ptit.com.enghub.dto.response.AchievementProgressResponse;
import ptit.com.enghub.dto.response.AchievementSummaryResponse;
import ptit.com.enghub.entity.Achievement;
import ptit.com.enghub.entity.AchievementProgress;
import ptit.com.enghub.entity.User;
import ptit.com.enghub.mapper.AchievementProgressMapper;
import ptit.com.enghub.repository.AchievementProgressRepository;
import ptit.com.enghub.repository.AchievementRepository;
import ptit.com.enghub.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final UserService userService;
    private final AchievementProgressRepository progressRepository;
    private final AchievementProgressMapper progressMapper;


    public AchievementSummaryResponse getSummary() {
        User user = userService.getCurrentUser();

        List<Object[]> rows =
                achievementRepository.findAllWithProgress(user.getId());

        List<AchievementProgressResponse> achievementProgress =
                rows.stream()
                        .map(r -> progressMapper.toResponse(
                                (Achievement) r[0],
                                (AchievementProgress) r[1]
                        ))
                        .toList();


        List<AchievementProgressResponse> achievementComplete = progressRepository.findCompletedProgress(user.getId())
                .stream()
                .map(p -> {
                    Achievement a = achievementRepository.findById(p.getAchievementId()).orElseThrow();
                    return progressMapper.toResponse(a, p);
                })
                .toList();
        AchievementSummaryResponse summaryResponse = new AchievementSummaryResponse();
        summaryResponse.setAchievementCompleteList(achievementComplete);
        summaryResponse.setAchievementProgressList(achievementProgress);

        return summaryResponse;
    }
}
