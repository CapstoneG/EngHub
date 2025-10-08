package ptit.com.enghub.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class LessonResponse {
    private Long id;
    private String title;
    private int orderIndex;
    private List<ExerciseResponse> exercises;
}
