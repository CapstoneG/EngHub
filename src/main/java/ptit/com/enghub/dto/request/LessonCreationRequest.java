package ptit.com.enghub.dto.request;

import lombok.Data;
import ptit.com.enghub.dto.*;

import java.util.List;

@Data
public class LessonCreationRequest {
    private String title;
    private int orderIndex;
    private int duration;
    private Long unitId;

    private VideoDTO video;
    private GrammarDTO grammar;
    private List<VocabularyDTO> vocabularies;
    private List<DialogueDTO> dialogues;
    private List<ExerciseDTO> exercises;
}
