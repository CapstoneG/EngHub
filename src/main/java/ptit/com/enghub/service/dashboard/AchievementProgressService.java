package ptit.com.enghub.service.dashboard;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ptit.com.enghub.entity.Achievement;
import ptit.com.enghub.entity.AchievementProgress;
import ptit.com.enghub.repository.AchievementProgressRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AchievementProgressService {

    private final AchievementProgressRepository progressRepo;

    @Transactional
    public void updateProgress(
            Long userId,
            Achievement achievement,
            int deltaValue
    ) {

        AchievementProgress progress = progressRepo
                .findByUserIdAndAchievementId(userId, achievement.getId())
                .orElseGet(() -> createNewProgress(userId, achievement));

        progress.setCurrentValue(
                Math.min(progress.getCurrentValue() + deltaValue, progress.getTargetValue())
        );
        progress.setLastUpdatedAt(LocalDateTime.now());

        progressRepo.save(progress);
    }

    private AchievementProgress createNewProgress(Long userId, Achievement achievement) {
        AchievementProgress progress = new AchievementProgress();
        progress.setUserId(userId);
        progress.setAchievementId(achievement.getId());
        progress.setCurrentValue(0);
        progress.setTargetValue(achievement.getConditionValue());
        progress.setLastUpdatedAt(LocalDateTime.now());
        return progress;
    }
}

