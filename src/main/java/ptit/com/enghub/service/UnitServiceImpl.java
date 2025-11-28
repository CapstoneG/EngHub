package ptit.com.enghub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ptit.com.enghub.dto.request.UnitRequest;
import ptit.com.enghub.dto.response.LessonResponse;
import ptit.com.enghub.dto.response.UnitResponse;
import ptit.com.enghub.entity.Course;
import ptit.com.enghub.entity.Unit;
import ptit.com.enghub.entity.UserProgress;
import ptit.com.enghub.mapper.UnitMapper;
import ptit.com.enghub.repository.CourseRepository;
import ptit.com.enghub.repository.UnitRepository;
import ptit.com.enghub.repository.UserProgressRepository;
import ptit.com.enghub.service.IService.UnitService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UnitServiceImpl implements UnitService {
    private final UnitRepository unitRepository;
    private final UnitMapper unitMapper;
    private final CourseRepository courseRepository;
    private final UserProgressRepository userProgressRepository;

    @Override
    public UnitResponse getUnitById(Long id) {
        Unit unit = unitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Unit not found"));
        return unitMapper.toResponse(unit);
    }

    @Override
    public List<UnitResponse> getUnitsByCourseId(Long courseId, Long userId) {
        List<UnitResponse> units = unitRepository.findByCourse_Id(courseId).stream()
                .map(unitMapper::toResponse)
                .peek(unit -> {
                    unit.setLessons(
                            unit.getLessons().stream()
                                    .sorted(Comparator.comparing(LessonResponse::getOrderIndex))
                                    .peek(l -> {
                                        boolean completed = userProgressRepository
                                                .findByUserIdAndLessonId(userId, l.getId())
                                                .map(UserProgress::isCompleted)
                                                .orElse(false);
                                        l.setCompleted(completed);
                                    })
                                    .collect(Collectors.toList())
                    );
                })
                .collect(Collectors.toList());
        return units;
    }

    @Override
    @Transactional
    public UnitResponse createUnit(UnitRequest request) {
        Unit unit = unitMapper.toEntity(request);

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));
        unit.setCourse(course);

        return unitMapper.toResponse(unitRepository.save(unit));
    }

    @Override
    @Transactional
    public UnitResponse updateUnit(Long id, UnitRequest request) {
        Unit unit = unitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Unit not found"));

        unitMapper.updateUnitFromRequest(request, unit);

        if (request.getCourseId() != null) {
            Course course = courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found"));
            unit.setCourse(course);
        }

        return unitMapper.toResponse(unitRepository.save(unit));
    }

    @Override
    @Transactional
    public void deleteUnit(Long id) {
        if (!unitRepository.existsById(id)) {
            throw new RuntimeException("Unit not found");
        }
        unitRepository.deleteById(id);
    }
}
