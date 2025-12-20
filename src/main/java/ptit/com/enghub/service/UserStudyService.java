package ptit.com.enghub.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ptit.com.enghub.entity.UserStudyDaily;
import ptit.com.enghub.entity.UserStudySession;
import ptit.com.enghub.repository.UserStudyDailyRepository;
import ptit.com.enghub.repository.UserStudySessionRepository;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class UserStudyService {

    private final UserStudyDailyRepository dailyRepo;
    private final UserStudySessionRepository userStudySessionRepository;

    @Transactional
    public void finishStudySession(UserStudySession session) {

        userStudySessionRepository.save(session);

        LocalDate studyDate = session.getStartedAt().toLocalDate();

        UserStudyDaily daily = dailyRepo
                .findByUserIdAndStudyDate(session.getUserId(), studyDate)
                .orElse(
                        UserStudyDaily.builder()
                                .userId(session.getUserId())
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
        daily.setLastStudyAt(session.getEndedAt());

        dailyRepo.save(daily);
    }

}
