package ptit.com.enghub.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class LessonCreationRequest {
    private String title;
    private int orderIndex;
    private int duration;
    private Long unitId;

    private VideoRequest video;
    private GrammarRequest grammar;
    private List<VocabularyRequest> vocabularies;
    private List<DialogueRequest> dialogues;
    private List<ExerciseRequest> exercises;
}
