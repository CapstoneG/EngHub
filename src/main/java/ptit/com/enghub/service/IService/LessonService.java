package ptit.com.enghub.service.IService;

import ptit.com.enghub.dto.request.CompleteLessonRequest;
import ptit.com.enghub.dto.request.LessonCreationRequest;
import ptit.com.enghub.dto.response.LessonResponse;
import ptit.com.enghub.entity.Lesson;

import java.util.List;

public interface LessonService {
    LessonResponse getLesson(Long lessonId, Long userId);
    List<LessonResponse> getLessonsByUnitId(Long unitId);
    void completeLesson(Long lessonId, CompleteLessonRequest request, Long userId);
    Lesson createLesson(LessonCreationRequest request);
    void deleteLesson(Long id);

}