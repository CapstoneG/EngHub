package ptit.com.enghub.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import ptit.com.enghub.enums.ExerciseType;

@Data
public class ExerciseDTO {
    private String question;
    private ExerciseType type;
    private JsonNode metadata;
}
