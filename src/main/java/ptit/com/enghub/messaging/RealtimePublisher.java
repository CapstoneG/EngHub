package ptit.com.enghub.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import ptit.com.enghub.entity.Notification;

@Component
@RequiredArgsConstructor
public class RealtimePublisher {
    private final SimpMessagingTemplate messagingTemplate;
    public void publishToUser(String userId, Notification notification) {
        messagingTemplate.convertAndSend(
                "/topic/notifications/" + userId,
                notification
        );
    }
}
