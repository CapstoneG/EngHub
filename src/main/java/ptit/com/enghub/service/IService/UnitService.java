package ptit.com.enghub.service.IService;

import ptit.com.enghub.dto.response.UnitResponse;

import java.util.List;

public interface UnitService {
    UnitResponse getUnitById(Long id);
    List<UnitResponse> getUnitsByCourseId(Long courseId);
}
