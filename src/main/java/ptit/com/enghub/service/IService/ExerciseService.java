package ptit.com.enghub.service.IService;

import ptit.com.enghub.dto.ExerciseDTO;

import java.util.List;

public interface ExerciseService {
    List<ExerciseDTO> getExercisesByLessonId(Long lessonId);
}
