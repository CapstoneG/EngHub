package ptit.com.enghub.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ptit.com.enghub.entity.UserStudySession;
import ptit.com.enghub.repository.UserStudySessionRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SessionsScheduler {

    private final UserStudySessionRepository sessionRepo;

    @Scheduled(fixedRate = 60000) // mỗi 1 phút
    public void autoEndStaleSessions() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(15);
        List<UserStudySession> stale = sessionRepo.findByEndedAtIsNullAndStartedAtBefore(threshold);

        for (UserStudySession s : stale) {
            s.setEndedAt(s.getStartedAt().plusMinutes(1));
            s.setDurationMinutes(1);
        }

        sessionRepo.saveAll(stale);
    }

}
