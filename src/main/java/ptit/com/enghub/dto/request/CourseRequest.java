package ptit.com.enghub.dto.request;

import lombok.Data;

@Data
public class CourseRequest {
    private String title;
    private String description;
    private String level;
    private Long languageId;
}
