package ptit.com.enghub.service.dashboard;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ptit.com.enghub.dto.request.NotificationRequest;
import ptit.com.enghub.entity.Achievement;
import ptit.com.enghub.entity.AchievementProgress;
import ptit.com.enghub.enums.NotificationType;
import ptit.com.enghub.repository.AchievementProgressRepository;
import ptit.com.enghub.service.NotificationService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AchievementProgressService {

    private final AchievementProgressRepository progressRepo;
    private final NotificationService notificationService;

    @Transactional
    public void updateProgress(
            Long userId,
            Achievement achievement,
            int deltaValue
    ) {

        AchievementProgress progress = progressRepo
                .findByUserIdAndAchievementId(userId, achievement.getId())
                .orElseGet(() -> createNewProgress(userId, achievement));

        int before = progress.getCurrentValue();
        int after = Math.min(
                before + deltaValue,
                progress.getTargetValue()
        );

        progress.setCurrentValue(after);
        progress.setLastUpdatedAt(LocalDateTime.now());


        boolean justCompleted =
                before < progress.getTargetValue()
                        && after == progress.getTargetValue();

        if (justCompleted) {
            NotificationRequest req = new NotificationRequest();
            req.setUserId(userId.toString());
            req.setType(NotificationType.ACHIEVEMENT);
            req.setTitle("Thành tựu hoàn thành!");
            req.setContent("Bạn đã hoàn thành: " + achievement.getName());
            notificationService.create(req);
        }

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

