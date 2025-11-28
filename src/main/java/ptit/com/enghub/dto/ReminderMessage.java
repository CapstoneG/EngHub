package ptit.com.enghub.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReminderMessage implements Serializable {
    private String userId;
    private String type;
    private String title;
    private String content;
}
