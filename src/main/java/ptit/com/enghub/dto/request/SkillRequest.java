package ptit.com.enghub.dto.request;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class SkillRequest {
    private String title;
    private String level;
    private String topic;
    private String thumbnail;
    private String skillType;
    private String mediaUrl;
    private JsonNode metadata;
}