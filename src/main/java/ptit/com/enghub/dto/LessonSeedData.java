package ptit.com.enghub.dto;

import lombok.Data;

import java.util.List;

@Data
public class LessonSeedData {
    private VideoDTO video;
    private GrammarDTO grammar;
    private List<VocabularyDTO> vocabularies;
    private List<DialogueDTO> dialogues;
    private List<ExerciseDTO> exercises;
}
