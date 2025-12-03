package ptit.com.enghub.dto.request;

import lombok.Getter;
import lombok.Setter;
import ptit.com.enghub.enums.NotificationType;

@Getter
@Setter
public class NotificationRequest {
    private String userId;
    private NotificationType type;
    private String title;
    private String content;
}
