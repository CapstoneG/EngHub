package ptit.com.enghub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ptit.com.enghub.dto.response.ApiResponse;
import ptit.com.enghub.dto.response.dashboard.ActivityDataResponse;
import ptit.com.enghub.dto.response.dashboard.ActivitySkillResponse;
import ptit.com.enghub.dto.response.dashboard.AdminDashboardResponse;
import ptit.com.enghub.service.dashboard.DashboardService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final DashboardService dashboardService;

    @GetMapping("/dashboard")
    public ApiResponse<AdminDashboardResponse> getDashboard() {

        AdminDashboardResponse response = dashboardService.getDashboard();
        return ApiResponse.<AdminDashboardResponse>builder()
                .result(response)
                .message("Dashboard data fetched successfully")
                .build();
    }
}
