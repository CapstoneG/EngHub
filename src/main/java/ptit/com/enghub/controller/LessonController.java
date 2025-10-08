package ptit.com.enghub.controller;

import com.cloudinary.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ptit.com.enghub.dto.request.CompleteLessonRequest;
import ptit.com.enghub.dto.response.ApiResponse;
import ptit.com.enghub.dto.response.ExerciseResponse;
import ptit.com.enghub.dto.response.LessonResponse;
import ptit.com.enghub.service.IService.ExerciseService;
import ptit.com.enghub.service.IService.LessonService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;
    private final ExerciseService exerciseService;

    @GetMapping("/lessons/{id}")
    public ApiResponse<LessonResponse> getLesson(@PathVariable Long id, @RequestParam Long userId) {
        return ApiResponse.<LessonResponse>builder()
                .result(lessonService.getLesson(id, userId))
                .build();
    }

    @GetMapping("/units/{unitId}/lessons")
    public ApiResponse<List<LessonResponse>> getLessonsByUnitId(@PathVariable Long unitId) {
        return ApiResponse.<List<LessonResponse>>builder()
                .result(lessonService.getLessonsByUnitId(unitId))
                .build();
    }

    @PostMapping("/lessons/{id}/complete")
    public ApiResponse<Void> completeLesson(@PathVariable Long id, @RequestBody CompleteLessonRequest request) {
        lessonService.completeLesson(id, request);
        return ApiResponse.<Void>builder()
                .message("Lesson completed successfully")
                .build();
    }

    @GetMapping("/lessons/{lessonId}/exercises")
    public ApiResponse<List<ExerciseResponse>> getExercisesByLessonId(@PathVariable Long lessonId) {
        return ApiResponse.<List<ExerciseResponse>>builder()
                .result(exerciseService.getExercisesByLessonId(lessonId))
                .build();
    }
}
