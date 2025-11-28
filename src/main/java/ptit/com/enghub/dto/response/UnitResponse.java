package ptit.com.enghub.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class UnitResponse {
    private Long id;
    private String title;
    private String description;
    private int orderIndex;
    private List<LessonResponse> lessons;
    private int progress;
    private String icon;
}
