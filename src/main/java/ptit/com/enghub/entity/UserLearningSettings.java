package ptit.com.enghub.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "user_learning_settings")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLearningSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "daily_study_reminder")
    private boolean dailyStudyReminder;

    @Column(name = "reminder_time")
    private LocalTime reminderTime;

    @Column(name = "email_notification")
    private boolean emailNotification;

    @Column(name = "daily_study_minutes")
    private Integer dailyStudyMinutes;

    @Column(name = "target_days_per_week")
    private Integer targetDaysPerWeek;
}
