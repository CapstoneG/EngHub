package ptit.com.enghub.service.dashboard;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ptit.com.enghub.dto.EndStudyDto;
import ptit.com.enghub.dto.request.StartStudyRequest;
import ptit.com.enghub.dto.response.StudyChartResponse;
import ptit.com.enghub.entity.User;
import ptit.com.enghub.entity.UserStudyDaily;
import ptit.com.enghub.entity.UserStudySession;
import ptit.com.enghub.exception.AppException;
import ptit.com.enghub.exception.ErrorCode;
import ptit.com.enghub.repository.UserStudyDailyRepository;
import ptit.com.enghub.repository.UserStudySessionRepository;
import ptit.com.enghub.service.UserService;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyTrackingService {

    private final UserService userService;
    private final UserStudySessionRepository sessionRepo;
    private final UserStudyDailyRepository dailyRepo;

    @Transactional
    public EndStudyDto startStudy(StartStudyRequest request) {
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
        EndStudyDto response = new EndStudyDto();
        response.setSessionId(session.getId());
        return response;
    }

    @Transactional
    public void endStudy(EndStudyDto request) {
        User user = userService.getCurrentUser();
        UserStudySession session;
        Long sessionId = request.getSessionId();
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

    public List<StudyChartResponse> getLast4WeeksWeekdayChart() {
        User user = userService.getCurrentUser();

        LocalDate today = LocalDate.now();
        LocalDate currentWeekMonday = today.with(DayOfWeek.MONDAY);

        LocalDate startMonday = currentWeekMonday.minusWeeks(3);

        List<Object[]> rawData =
                dailyRepo.findDailyStudyMinutes(user.getId(), startMonday);

        Map<LocalDate, Integer> minutesMap = new HashMap<>();

        for (Object[] row : rawData) {
            LocalDate date = (LocalDate) row[0];
            Number minutes = (Number) row[1];

            minutesMap.put(
                    date,
                    minutes != null ? minutes.intValue() : 0
            );
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
        List<StudyChartResponse> chartData = new ArrayList<>();

        for (int week = 0; week < 4; week++) {
            LocalDate weekMonday = startMonday.plusWeeks(week);
            for (int day = 0; day < 7; day++) {
                LocalDate date = weekMonday.plusDays(day);

                chartData.add(new StudyChartResponse(
                        date.format(formatter),
                        minutesMap.getOrDefault(date, 0)
                ));
            }
        }

        return chartData;
    }


}
