package ptit.com.enghub.dto;

import lombok.Data;
import ptit.com.enghub.enums.NotificationType;

import java.io.Serializable;

@Data
public class ReminderMessage implements Serializable {
    private String userId;
    private NotificationType type;
    private String title;
    private String content;
}
