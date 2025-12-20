package ptit.com.enghub.service.dashboard;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ptit.com.enghub.dto.request.EndStudyRequest;
import ptit.com.enghub.dto.request.StartStudyRequest;
import ptit.com.enghub.entity.User;
import ptit.com.enghub.entity.UserStudyDaily;
import ptit.com.enghub.entity.UserStudySession;
import ptit.com.enghub.exception.AppException;
import ptit.com.enghub.exception.ErrorCode;
import ptit.com.enghub.repository.UserStudyDailyRepository;
import ptit.com.enghub.repository.UserStudySessionRepository;
import ptit.com.enghub.service.AuthenticationServiceImpl;
import ptit.com.enghub.service.UserService;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StudyTrackingService {

    private final UserService userService;
    private final UserStudySessionRepository sessionRepo;
    private final UserStudyDailyRepository dailyRepo;
    private final AuthenticationServiceImpl authenticationService;

    @Transactional
    public void startStudy(StartStudyRequest request) {
        User user = userService.getCurrentUser();

        UserStudySession session = UserStudySession.builder()
                .userId(user.getId())
                .startedAt(LocalDateTime.now())
                .activityType(request.getActivityType())
                .skill(request.getSkill())
                .lessonId(request.getLessonId())
                .deckId(request.getDeckId())
                .build();

        sessionRepo.save(session);
    }

    @Transactional
    public void endStudy(Long sessionId, String token) {

        User user = authenticationService.getUserFromToken(token);
        UserStudySession session;

        if (sessionId != null) {
            session = sessionRepo.findById(sessionId)
                    .orElseThrow(() -> new RuntimeException("Session not found"));
        } else {
            session = sessionRepo
                    .findTopByUserIdOrderByStartedAtDesc(user.getId())
                    .orElseThrow(() -> new RuntimeException("No active session"));
        }

        if (!session.getUserId().equals(user.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        if (session.getEndedAt() != null) return;

        LocalDateTime now = LocalDateTime.now();

        session.setEndedAt(now);

        int minutes = (int) Duration.between(
                session.getStartedAt(), now
        ).toMinutes();

        session.setDurationMinutes(Math.max(minutes, 1));

        sessionRepo.save(session);
        LocalDate studyDate = session.getStartedAt().toLocalDate();

        UserStudyDaily daily = dailyRepo
                .findByUserIdAndStudyDate(user.getId(), studyDate)
                .orElse(
                        UserStudyDaily.builder()
                                .userId(user.getId())
                                .studyDate(studyDate)
                                .totalMinutes(0)
                                .sessionCount(0)
                                .firstStudyAt(session.getStartedAt())
                                .build()
                );

        daily.setTotalMinutes(
                daily.getTotalMinutes() + session.getDurationMinutes()
        );
        daily.setSessionCount(daily.getSessionCount() + 1);
        daily.setLastStudyAt(now);

        dailyRepo.save(daily);
    }
}
