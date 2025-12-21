package ptit.com.enghub.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RealtimePublisher {

    private final SimpMessagingTemplate messagingTemplate;
    public RealtimePublisher(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void publishToUser(String channel, String userId, Object payload) {
        messagingTemplate.convertAndSend("/topic/" + channel + "/" + userId, payload);
    }
}
