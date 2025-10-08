package ptit.com.enghub.dto.response;

import lombok.Data;
import ptit.com.enghub.enums.ExerciseType;

import java.util.List;

@Data
public class ExerciseResponse {
    private Long id;
    private String question;
    private ExerciseType type;
    private List<ExerciseOptionResponse> options;
}
