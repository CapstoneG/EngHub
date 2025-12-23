package ptit.com.enghub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptit.com.enghub.dto.response.AchievementSummaryResponse;
import ptit.com.enghub.service.dashboard.AchievementService;


@RestController
@RequestMapping("/api/achievements")
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService achievementService;

    @GetMapping()
    public ResponseEntity<AchievementSummaryResponse> getAchievementsUser() {
        return ResponseEntity.ok(
                achievementService.getSummary()
        );
    }

}
