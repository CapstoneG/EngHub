package ptit.com.enghub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptit.com.enghub.dto.ReminderMessage;
import ptit.com.enghub.dto.request.NotificationRequest;
import ptit.com.enghub.entity.Notification;
import ptit.com.enghub.entity.User;
import ptit.com.enghub.enums.NotificationType;
import ptit.com.enghub.messaging.NotificationProducer;
import ptit.com.enghub.repository.NotificationRepository;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;
    private final RealtimePublisher publisher;
    private final UserService userService;

    public Notification create(NotificationRequest req) {

        Notification n = new Notification();
        n.setId(UUID.randomUUID());
        n.setUserId(req.getUserId());
        n.setType(req.getType());
        n.setTitle(req.getTitle());
        n.setContent(req.getContent());
        n.setCreatedAt(Instant.now());
        n.setRead(false);

        repository.save(n);
        publisher.publishToUser("notifications", req.getUserId(), n);
        return n;
    }

    public Page<Notification> listForUser(Pageable pageable) {
        User user = userService.getCurrentUser();
        return repository.findByUserIdOrderByCreatedAtDesc(user.getId().toString(), pageable);
    }

    public void markAsRead(UUID id) {
        repository.findById(id).ifPresent(n -> {
            n.setRead(true);
            n.setReadAt(Instant.now());
            repository.save(n);
        });
    }

    public long countUnread() {
        User user = userService.getCurrentUser();
        return repository.countByUserIdAndIsReadFalse(user.getId().toString());
    }
}

