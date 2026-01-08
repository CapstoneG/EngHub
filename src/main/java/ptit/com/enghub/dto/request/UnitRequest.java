package ptit.com.enghub.dto.request;

import lombok.Data;

@Data
public class UnitRequest {
    private String title;
    private int orderIndex;
    private Long courseId;
    private String description;
    private String icon;
}
