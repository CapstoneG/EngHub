package ptit.com.enghub.dto.response;

import lombok.Data;
import ptit.com.enghub.dto.*;

import java.util.List;

@Data
public class LessonResponse {
    private Long id;
    private String title;
    private int orderIndex;
    private int duration;

    private List<ExerciseDTO> exercises;
    private List<DialogueDTO> dialogues;
    private GrammarDTO grammar;
    private VideoDTO video;
    private List<VocabularyDTO> vocabularies;
    private boolean completed;
    private String studySkill;
}
