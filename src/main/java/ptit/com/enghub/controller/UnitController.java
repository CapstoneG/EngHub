package ptit.com.enghub.controller;

import com.cloudinary.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptit.com.enghub.dto.response.ApiResponse;
import ptit.com.enghub.dto.response.UnitResponse;
import ptit.com.enghub.entity.Unit;
import ptit.com.enghub.service.IService.UnitService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UnitController {
    private final UnitService unitService;

    @GetMapping("/units/{id}")
    public ApiResponse<UnitResponse> getUnitById(@PathVariable Long id) {
        return ApiResponse.<UnitResponse>builder()
                .result(unitService.getUnitById(id))
                .build();
    }

    @GetMapping("/courses/{courseId}/units")
    public ApiResponse<List<UnitResponse>> getUnitsByCourseId(@PathVariable Long courseId) {
        return ApiResponse.<List<UnitResponse>>builder()
                .result(unitService.getUnitsByCourseId(courseId))
                .build();
    }
}
