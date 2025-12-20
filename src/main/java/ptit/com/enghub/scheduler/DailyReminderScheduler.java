package ptit.com.enghub.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ptit.com.enghub.entity.User;
import ptit.com.enghub.entity.UserLearningSettings;
import ptit.com.enghub.repository.UserRepository;
import ptit.com.enghub.repository.UserSettingsRepository;
import ptit.com.enghub.service.IService.EmailService;
import ptit.com.enghub.service.NotificationService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
@Slf4j
public class DailyReminderScheduler {

//    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final UserSettingsRepository preferenceRepository;

    public DailyReminderScheduler(NotificationService notificationService,
                                  UserRepository userRepository,
                                  EmailService emailService,
                                  UserSettingsRepository preferenceRepository) {
//        this.notificationService = notificationService;
        this.preferenceRepository = preferenceRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

//    @Scheduled(cron = "0 7 0 * * *", zone = "Asia/Bangkok")
//    public void sendDailyReminders() {
//        LocalDateTime threshold = LocalDateTime.now().minusDays(1);
//        List<User> users = userRepository.findUsersLoggedWithin(threshold);
//        for (User u : users) {
////            notificationService.enqueueReminder(u.getId().toString(), "Đã đến giờ học!", "Hôm nay học 10 phút thôi nào!");
//            emailService.sendEmailWithHtmlTemplate(
//                    u.getEmail(),
//                    "Nhắc nhở học tiếng Anh mỗi ngày",
//                    "daily-reminder",
//                    null
//            );
//            log.info("mail");
//        }
//    }

    @Scheduled(cron = "0 0 * * * *", zone = "Asia/Bangkok")
    public void sendDailyReminders() {

        LocalTime now = LocalTime.now().withSecond(0).withNano(0);

        List<UserLearningSettings> preferences =
                preferenceRepository.findUsersNeedDailyEmail(now);

        for (UserLearningSettings pref : preferences) {
            try {
                emailService.sendEmailWithHtmlTemplate(
                        pref.getUser().getEmail(),
                        "Nhắc nhở học tiếng Anh mỗi ngày",
                        "daily-reminder",
                        null
                );

                log.info("Sent daily reminder to {}", pref.getUser().getEmail());
            } catch (Exception e) {
                log.error("Failed to send reminder to {}", pref.getUser().getEmail(), e);
            }
        }
    }
}