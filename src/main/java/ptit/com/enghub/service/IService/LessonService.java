package ptit.com.enghub.service.IService;

import ptit.com.enghub.dto.LessonSeedData;
import ptit.com.enghub.dto.request.CompleteLessonRequest;
import ptit.com.enghub.dto.request.LessonCreationRequest;
import ptit.com.enghub.dto.response.LessonResponse;
import ptit.com.enghub.entity.Lesson;

import java.util.List;

public interface LessonService {
    LessonResponse getLesson(Long lessonId);
    List<LessonResponse> getLessonsByUnitId(Long unitId);
    void completeLesson(Long lessonId, CompleteLessonRequest request);
    Lesson createLesson(LessonCreationRequest request);
    void deleteLesson(Long id);
    void seedLesson(Long lessonId, LessonSeedData data);

}