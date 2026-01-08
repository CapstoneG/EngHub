package ptit.com.enghub.service.dashboard;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ptit.com.enghub.dto.response.dashboard.ActivityDataResponse;
import ptit.com.enghub.dto.response.dashboard.ActivitySkillResponse;
import ptit.com.enghub.dto.response.dashboard.AdminDashboardResponse;
import ptit.com.enghub.enums.StudyActivityType;
import ptit.com.enghub.enums.StudySkill;
import ptit.com.enghub.repository.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final SkillRepository skillRepository;
    private final DeckRepository deckRepository;
    private final UserStudySessionRepository sessionRepository;

    public AdminDashboardResponse getDashboard() {

        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countActiveUsers();
        long totalLessons = lessonRepository.count();
        long totalSkills = skillRepository.count();
        long totalDeck = deckRepository.count();

        List<ActivityDataResponse> activityDataResponses = getWeeklyActivityData();
        List<ActivitySkillResponse> activitySkillData = getActivitySkillData();
        return AdminDashboardResponse.builder()
                .totalUsers(totalUsers)
                .totalLessons(totalLessons)
                .totalSkills(totalSkills)
                .activeUsers(activeUsers)
                .totalDeck(totalDeck)
                .activityData(activityDataResponses)
                .activityDataSkill(activitySkillData)
                .build();
    }

    public List<ActivityDataResponse> getWeeklyActivityData() {

        LocalDateTime mondayStart = LocalDate.now()
                .with(DayOfWeek.MONDAY)
                .atStartOfDay();

        List<Object[]> rawData =
                sessionRepository.countActivityByDay(mondayStart);

        Map<Integer, ActivityDataResponse> map = new HashMap<>();

        DayOfWeek today = LocalDate.now().getDayOfWeek();
        int todayValue = today.getValue();
        for (int i = 1; i <= todayValue; i++) {
            map.put(i, new ActivityDataResponse("T" + (i + 1), 0, 0, 0));
        }

        for (Object[] row : rawData) {
            Integer dow = ((Number) row[0]).intValue(); // 1..5
            StudyActivityType type = (StudyActivityType) row[1];
            int count = (int) row[2];

            ActivityDataResponse data = map.get(dow);
            if (data == null) continue;

            switch (type) {
                case FLASHCARD -> data.setFlashcards(count);
                case LESSON -> data.setLessons(count);
                case SKILL -> data.setSkills(count);
            }
        }

        return map.values()
                .stream()
                .sorted(Comparator.comparing(ActivityDataResponse::getName))
                .toList();
    }

    public List<ActivitySkillResponse> getActivitySkillData() {

        Map<StudySkill, Integer> skillMap = new EnumMap<>(StudySkill.class);
        for (StudySkill skill : StudySkill.values()) {
            skillMap.put(skill, 0);
        }

        sessionRepository.countDistinctUsersBySkill()
                .forEach(row -> {
                    StudySkill skill = (StudySkill) row[0];
                    int count = ((Long) row[1]).intValue();
                    skillMap.put(skill, count);
                });
        return skillMap.entrySet().stream()
                .map(e -> new ActivitySkillResponse(
                        e.getKey().name(),
                        e.getValue()
                ))
                .toList();
    }
}
