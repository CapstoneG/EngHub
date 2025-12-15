package ptit.com.enghub.service.IService;

import java.util.List;

public interface ExerciseService {
    List<ExerciseResponse> getExercisesByLessonId(Long lessonId);
}
