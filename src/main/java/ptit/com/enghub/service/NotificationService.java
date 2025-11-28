package ptit.com.enghub.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptit.com.enghub.dto.ReminderMessage;
import ptit.com.enghub.dto.request.NotificationRequest;
import ptit.com.enghub.entity.Notification;
import ptit.com.enghub.messaging.NotificationProducer;
import ptit.com.enghub.repository.NotificationRepository;

import java.time.Instant;
import java.util.UUID;

@Service
public class NotificationService {

    private final NotificationRepository repository;
    private final RealtimePublisher realtimePublisher;
    private final NotificationProducer producer;

    public NotificationService(NotificationRepository repository,
                               RealtimePublisher realtimePublisher,
                               NotificationProducer producer) {
        this.repository = repository;
        this.realtimePublisher = realtimePublisher;
        this.producer = producer;
    }

    public Notification createAndSend(NotificationRequest req) {
        Notification n = new Notification();
        n.setId(UUID.randomUUID());
        n.setUserId(req.userId);
        n.setType(req.type);
        n.setTitle(req.title);
        n.setContent(req.content);
        n.setCreatedAt(Instant.now());
        n.setRead(false);

        repository.save(n);
        realtimePublisher.publishToUser(n.getUserId(), n);
        return n;
    }

    public void enqueueReminder(String userId, String title, String content) {
        var msg = new ReminderMessage();
        msg.setUserId(userId);
        msg.setType("DAILY_REMINDER");
        msg.setTitle(title);
        msg.setContent(content);
        producer.sendReminder(msg);
    }

    public Page<Notification> listForUser(String userId, Pageable pageable) {
        return repository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    public void markAsRead(UUID id) {
        repository.findById(id).ifPresent(n -> {
            n.setRead(true);
            n.setReadAt(Instant.now());
            repository.save(n);
        });
    }

    public long countUnread(String userId) {
        return repository.countByUserIdAndIsReadFalse(userId);
    }
}
