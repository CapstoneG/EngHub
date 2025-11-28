package ptit.com.enghub.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ptit.com.enghub.config.RabbitMQConfig;
import ptit.com.enghub.dto.ReminderMessage;
import ptit.com.enghub.entity.Notification;
import ptit.com.enghub.repository.NotificationRepository;
import ptit.com.enghub.service.RealtimePublisher;

import java.time.Instant;
import java.util.UUID;

@Component
public class NotificationConsumer {

    private final NotificationRepository notificationRepository;
    private final RealtimePublisher realtimePublisher;

    public NotificationConsumer(NotificationRepository notificationRepository, RealtimePublisher realtimePublisher) {
        this.notificationRepository = notificationRepository;
        this.realtimePublisher = realtimePublisher;
    }

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    public void handleReminder(ReminderMessage msg) {
        Notification n = new Notification();
        n.setId(UUID.randomUUID());
        n.setUserId(msg.getUserId());
        n.setType(msg.getType());
        n.setTitle(msg.getTitle());
        n.setContent(msg.getContent());
        n.setCreatedAt(Instant.now());
        n.setRead(false);

        notificationRepository.save(n);
        realtimePublisher.publishToUser(msg.getUserId(), n);
    }
}
