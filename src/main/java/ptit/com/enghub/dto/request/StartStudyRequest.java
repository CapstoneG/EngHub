package ptit.com.enghub.dto.request;

import lombok.*;
import ptit.com.enghub.enums.StudyActivityType;
import ptit.com.enghub.enums.StudySkill;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StartStudyRequest {
    private StudyActivityType activityType;
    private StudySkill skill;
    private Long lessonId;
    private Long deckId;
}