package ptit.com.enghub.dto.response;

import java.time.Instant;
import java.util.UUID;

public class NotificationResponse {
    public UUID id;
    public String userId;
    public String type;
    public String title;
    public String content;
    public boolean isRead;
    public Instant createdAt;
    public Instant readAt;
}