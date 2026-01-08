package ptit.com.enghub.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillResponse {
    private Long id;
    private String title;
    private String level;
    private String mediaUrl;
    private String topic;
    private String thumbnail;
    private String skillType;
    private JsonNode metadata;
}
