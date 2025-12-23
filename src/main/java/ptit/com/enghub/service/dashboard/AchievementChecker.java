package ptit.com.enghub.service.dashboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ptit.com.enghub.entity.Achievement;
import ptit.com.enghub.enums.ConditionType;
import ptit.com.enghub.repository.AchievementRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AchievementChecker {

    private final AchievementRepository achievementRepo;
    private final AchievementProgressService progressService;

    public void onFlashcardStudied(Long userId, int cardsStudied) {

        List<Achievement> achievements = achievementRepo.findAll();

        for (Achievement achievement : achievements) {

            if (achievement.getConditionType() == ConditionType.TOTAL_CARDS) {
                progressService.updateProgress(
                        userId,
                        achievement,
                        cardsStudied
                );
            }
        }
    }

    public void onDailyStudy(Long userId, int streakDays) {

        List<Achievement> achievements = achievementRepo.findAll();

        for (Achievement achievement : achievements) {
            if (achievement.getConditionType() == ConditionType.STREAK_DAYS) {

                progressService.updateProgress(
                        userId,
                        achievement,
                        1
                );
            }
        }
    }

}
