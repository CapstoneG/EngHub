package ptit.com.enghub.service.IService;

import ptit.com.enghub.dto.response.ExerciseResponse;

import java.util.List;

public interface ExerciseService {
    List<ExerciseResponse> getExercisesByLessonId(Long lessonId);
}
