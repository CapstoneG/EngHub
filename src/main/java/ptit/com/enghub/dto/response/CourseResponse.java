package ptit.com.enghub.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class CourseResponse {
    private Long id;
    private String title;
    private String description;
    private String level;
    private LanguageResponse language;
    private List<UnitResponse> units;
}
