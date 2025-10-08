package ptit.com.enghub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ptit.com.enghub.dto.request.CompleteLessonRequest;
import ptit.com.enghub.dto.response.LessonResponse;
import ptit.com.enghub.entity.Lesson;
import ptit.com.enghub.entity.UserProgress;
import ptit.com.enghub.mapper.LessonMapper;
import ptit.com.enghub.repository.LessonRepository;
import ptit.com.enghub.repository.UserProgressRepository;
import ptit.com.enghub.service.IService.LessonService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final UserProgressRepository userProgressRepository;
    private final LessonMapper lessonMapper;

    @Override
    public LessonResponse getLesson(Long lessonId, Long userId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        // Kiểm tra quyền truy cập (bài học đã được mở khóa chưa)
        if (!isLessonUnlocked(lesson, userId)) {
            throw new RuntimeException("Lesson is locked. Complete previous lessons first.");
        }

        return lessonMapper.toResponse(lesson);
    }

    @Override
    public List<LessonResponse> getLessonsByUnitId(Long unitId) {
        return lessonRepository.findByUnit_Id(unitId).stream()
                .map(lessonMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void completeLesson(Long lessonId, CompleteLessonRequest request) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        UserProgress progress = userProgressRepository.findByUserIdAndLessonId(request.getUserId(), lessonId)
                .orElse(new UserProgress());

        progress.setUserId(request.getUserId());
        progress.setLesson(lesson);
        progress.setCompleted(true);
        progress.setScore(request.getScore());
        progress.setLastUpdated(LocalDateTime.now());

        userProgressRepository.save(progress);

        // Mở khóa bài học tiếp theo
        unlockNextLesson(lesson, request.getUserId());
    }

    private boolean isLessonUnlocked(Lesson lesson, Long userId) {
        // Bài học đầu tiên của unit luôn mở khóa
        if (lesson.getOrderIndex() == 0) {
            return true;
        }

        // Kiểm tra bài học trước đó
        Lesson previousLesson = lessonRepository.findByUnit_Id(lesson.getUnit().getId()).stream()
                .filter(l -> l.getOrderIndex() == lesson.getOrderIndex() - 1)
                .findFirst()
                .orElse(null);

        if (previousLesson == null) {
            return true;
        }

        return userProgressRepository.findByUserIdAndLessonId(userId, previousLesson.getId())
                .map(UserProgress::isCompleted)
                .orElse(false);
    }

    private void unlockNextLesson(Lesson currentLesson, Long userId) {
        Lesson nextLesson = lessonRepository.findByUnit_Id(currentLesson.getUnit().getId()).stream()
                .filter(l -> l.getOrderIndex() == currentLesson.getOrderIndex() + 1)
                .findFirst()
                .orElse(null);

        if (nextLesson != null) {
            UserProgress nextProgress = userProgressRepository.findByUserIdAndLessonId(userId, nextLesson.getId())
                    .orElse(new UserProgress());
            nextProgress.setUserId(userId);
            nextProgress.setLesson(nextLesson);
            nextProgress.setCompleted(false);
            nextProgress.setScore(0);
            nextProgress.setLastUpdated(LocalDateTime.now());
            userProgressRepository.save(nextProgress);
        }
    }
}
