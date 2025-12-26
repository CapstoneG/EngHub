package ptit.com.enghub.scheduler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ptit.com.enghub.repository.AchievementProgressRepository;
import ptit.com.enghub.repository.UserRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class StreakResetScheduler {

    private final UserRepository userRepository;
    private final AchievementProgressRepository achievementRepo;

    @Scheduled(
            cron = "0 1 0 * * *",
            zone = "Asia/Ho_Chi_Minh"
    )
    @Transactional
    public void resetMissedStreaks() {
        List<Long> userIds = userRepository.findAllUserIds();
        for (Long userId : userIds) {
            achievementRepo.resetStreakIfMissed(userId);
        }
    }
}
