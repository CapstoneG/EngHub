package ptit.com.enghub.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RealtimePublisher {

    private final SimpMessagingTemplate messagingTemplate;
    public RealtimePublisher(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // FE subscribes to /topic/notifications.{userId} (or /topic/notifications/{userId})
    public void publishToUser(String userId, Object payload) {
        messagingTemplate.convertAndSend("/topic/notifications/" + userId, payload);
    }
}
