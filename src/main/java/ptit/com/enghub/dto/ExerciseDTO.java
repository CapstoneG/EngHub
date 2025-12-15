package ptit.com.enghub.dto.request;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import ptit.com.enghub.enums.ExerciseType;

import java.util.List;

@Data
public class ExerciseRequest {
    private String question;
    private ExerciseType type;
    private JsonNode metadata;
}
