package ptit.com.enghub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ptit.com.enghub.dto.request.UnitRequest;
import ptit.com.enghub.dto.response.ApiResponse;
import ptit.com.enghub.dto.response.UnitResponse;
import ptit.com.enghub.entity.User;
import ptit.com.enghub.service.IService.UnitService;
import ptit.com.enghub.service.UserService;

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
                .message("Get unit detail successfully")
                .build();
    }

    @GetMapping("/courses/{courseId}/units")
    public ApiResponse<List<UnitResponse>> getUnitsByCourseId(@PathVariable Long courseId) {
        return ApiResponse.<List<UnitResponse>>builder()
                .result(unitService.getUnitsByCourseId(courseId))
                .message("Get units by course successfully")
                .build();
    }

    @PostMapping
    public ApiResponse<UnitResponse> create(@RequestBody UnitRequest request) {
        return ApiResponse.<UnitResponse>builder()
                .result(unitService.createUnit(request))
                .message("Unit created successfully")
                .build();
    }
    @PutMapping("/{id}")
    public ApiResponse<UnitResponse> update(@PathVariable Long id, @RequestBody UnitRequest request) {
        return ApiResponse.<UnitResponse>builder()
                .result(unitService.updateUnit(id, request))
                .message("Unit updated successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        unitService.deleteUnit(id);
        return ApiResponse.<Void>builder()
                .message("Unit deleted successfully")
                .build();
    }
}
