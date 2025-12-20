package ptit.com.enghub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptit.com.enghub.dto.request.EndStudyRequest;
import ptit.com.enghub.dto.request.StartStudyRequest;
import ptit.com.enghub.dto.response.ApiResponse;
import ptit.com.enghub.service.dashboard.StudyTrackingService;

@RestController
@RequestMapping("/api/v1/study")
@RequiredArgsConstructor
public class UserStudyController {

    private final StudyTrackingService studyTrackingService;

    @PostMapping("/start")
    public ResponseEntity<ApiResponse<Void>> startStudy(
            @RequestBody StartStudyRequest request
    ) {
        studyTrackingService.startStudy(request);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(0)
                        .message("start session")
                        .build()
        );
    }

    @PostMapping("/end")
    public ResponseEntity<ApiResponse<Void>> endStudy(
            @RequestParam("sessionId") Long sessionId,
            @RequestParam(value = "token", required = false) String token
    ) {
        studyTrackingService.endStudy(sessionId, "Bearer " + token);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(0)
                        .message("End session")
                        .build()
        );
    }
}

