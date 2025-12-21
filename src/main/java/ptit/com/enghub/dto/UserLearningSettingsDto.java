package ptit.com.enghub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLearningSettingsDto {
    Boolean dailyStudyReminder;
    LocalTime reminderTime;
    Boolean emailNotification;
    Integer dailyStudyMinutes;
    Integer targetDaysPerWeek;
}