package ptit.com.enghub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ptit.com.enghub.dto.response.UnitResponse;
import ptit.com.enghub.entity.Unit;
import ptit.com.enghub.mapper.UnitMapper;
import ptit.com.enghub.repository.UnitRepository;
import ptit.com.enghub.service.IService.UnitService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UnitServiceImpl implements UnitService {
    private final UnitRepository unitRepository;
    private final UnitMapper unitMapper;

    @Override
    public UnitResponse getUnitById(Long id) {
        Unit unit = unitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Unit not found"));
        return unitMapper.toResponse(unit);
    }

    @Override
    public List<UnitResponse> getUnitsByCourseId(Long courseId) {
        return unitRepository.findByCourse_Id(courseId).stream()
                .map(unitMapper::toResponse)
                .collect(Collectors.toList());
    }
}
