package ptit.com.enghub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ptit.com.enghub.dto.response.ExerciseResponse;
import ptit.com.enghub.mapper.ExerciseMapper;
import ptit.com.enghub.repository.ExerciseRepository;
import ptit.com.enghub.service.IService.ExerciseService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;

    @Override
    public List<ExerciseResponse> getExercisesByLessonId(Long lessonId) {
        return exerciseRepository.findByLesson_Id(lessonId).stream()
                .map(exerciseMapper::toResponse)
                .collect(Collectors.toList());
    }
}
